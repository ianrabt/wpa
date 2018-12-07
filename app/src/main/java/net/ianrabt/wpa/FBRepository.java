package net.ianrabt.wpa;

import android.support.annotation.NonNull;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.ianrabt.wpa.models.HabitModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FBRepository{

    private DatabaseReference mDatabase;
    private FBHabitDelegate habitDelegate;
    private FBDataDelegate dataDelegate;

    public FBRepository() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void setDataDelegate(FBDataDelegate dataDelegate) {
        this.dataDelegate = dataDelegate;
    }

    public void setHabitDelegate(FBHabitDelegate habitDelegate) {
        this.habitDelegate = habitDelegate;
    }

    public void createHabit(String habitName, List<Integer> repeatsOnDays, String time, Double lat, Double lon){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        String author = user.getDisplayName();
        String key = mDatabase.child("habits").push().getKey();
        HabitModel habit = new HabitModel(key, id, author, habitName, repeatsOnDays, time, lat, lon);
        Map<String, Object> habitValues = habit.toMap();
        Map<String, Object> data = new HashMap<>();
        data.put("daysPerWeek", repeatsOnDays.size());
        data.put("completions", 0);
        data.put("dateCreated", habit.getDateCreated());


        Map<String, Object>child = new HashMap<String, Object>();
        child.put("/habits/" + key, habitValues);
        child.put("/data/" + id + "/" + key, data);

        //update the days for which these habits go under
        List<Integer> days = habit.getRepeatsOnDays();
        for (int i = 0; i < habit.getRepeatsOnDays().size(); ++i){
            child.put("/userhabits/" + id + "/" + days.get(i) + "/" + key, habitValues);
        }


        mDatabase.updateChildren(child);
    }

    public void getHabits(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        Query query = mDatabase.child("userhabits").child(userId);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<HabitModel> habitList = new ArrayList<>();
                GenericTypeIndicator<List<Integer>> t = new GenericTypeIndicator<List<Integer>>() {};
                for (DataSnapshot habitSnapshot: dataSnapshot.getChildren()) {
                    HabitModel child = habitSnapshot.getValue(HabitModel.class);
                    List<Integer> days = habitSnapshot.child("repeats_on_days").getValue(t);
                    habitList.add(child);
                }
                habitDelegate.handleHabitResponse(habitList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //log
            }
        });


    }

    public void getHabitsByDay(String day){
        //mDatabase.addChildEventListener(mChildEventListener);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        Query query = mDatabase.child("userhabits").child(userId).child(day);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<HabitModel> habitList = new ArrayList<>();
                GenericTypeIndicator<List<Integer>> t = new GenericTypeIndicator<List<Integer>>() {};
                for (DataSnapshot habitSnapshot: dataSnapshot.getChildren()) {
                    HabitModel child = habitSnapshot.getValue(HabitModel.class);
                    List<Integer> days = habitSnapshot.child("repeatsOnDays").getValue(t);
                    habitList.add(child);
                }
                habitDelegate.handleHabitResponse(habitList);
                habitDelegate.render();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //log
            }
        });


    }

    public void getVisualizationData() {
        if(dataDelegate == null)
            return;
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        Query query = mDatabase.child("data").child(userId);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double numOccurrences = 0;
                double numCompletions = 0;
                Date today = Calendar.getInstance().getTime();

                for (DataSnapshot dataVisSnapshot : dataSnapshot.getChildren()) {
                    numCompletions += dataVisSnapshot.child("completions").getValue(Integer.class);

                    Long milliCreated = dataVisSnapshot.child("dateCreated").getValue(Long.class);
                    Date dateCreated = new Date(milliCreated);
                    Integer daysPerWeek = dataVisSnapshot.child("daysPerWeek").getValue(Integer.class);


                    double numWeeksSinceHabitCreated = getDateDiff(dateCreated, today, TimeUnit.DAYS)/7.0;
                    numOccurrences += daysPerWeek * numWeeksSinceHabitCreated;
                }
                //percentage of completion for all habits = numCompletions/numOccurrences

                double completionPercentage = numCompletions/numOccurrences;

                dataDelegate.handleData(completionPercentage);
                dataDelegate.render();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //log
            }
        });
    }

    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    //if increment is true, then increment the streak counter, otherwise decrement the streak counter
    public void updateCounts(String habitId, Integer currentStreakValue,
                             Integer currentCompletionValue, boolean increment,
                             List<Integer> repeatDays, String date){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        List<DatabaseReference> userHabitChildren = new ArrayList<>();
        for(Integer val : repeatDays){
            String day = Integer.toString(val);
            userHabitChildren.add(mDatabase.child("userhabits").child(userId).child(day).child(habitId));
        }
        DatabaseReference habitChild = mDatabase.child("habits").child(habitId);
        DatabaseReference dataChild = mDatabase.child("data").child(userId).child(habitId);

        // For the case where the checkbox is unclicked and needs to revert to previous day
        String newDate;
        if (date.isEmpty()) {
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat spf= new SimpleDateFormat("yyyy-MM-dd");
            newDate = spf.format(today);
        }
        else {
            newDate = date;
        }


        if(increment){
            updateCompletion(currentCompletionValue+1, true, userHabitChildren,
                    habitChild, dataChild, newDate);
            updateStreak(currentStreakValue+1, userHabitChildren, habitChild);
        } else{
            updateCompletion(currentCompletionValue-1, false, userHabitChildren,
                    habitChild, dataChild, newDate);
            updateStreak(currentStreakValue-1, userHabitChildren, habitChild);
        }


    }
    // TODO: need to update the previous Date + day?
    private void updateStreak(int newStreakValue, List<DatabaseReference> userHabitChildren,
                              DatabaseReference habitChild){
        for(DatabaseReference userHabitChild: userHabitChildren){
            userHabitChild.child("streakCounter").setValue(newStreakValue);
        }
        habitChild.child("streakCounter").setValue(newStreakValue);
    }

    private void updateCompletion(int newCompletionValue, boolean isChecked,
                                  List<DatabaseReference> userHabitChildren, DatabaseReference habitChild,
                                  DatabaseReference dataChild, String today){
        for(DatabaseReference userHabitChild: userHabitChildren){
            userHabitChild.child("checked").setValue(isChecked);
            userHabitChild.child("dateLastChecked").setValue(today);
            userHabitChild.child("completions").setValue(newCompletionValue);
        }
        habitChild.child("completions").setValue(newCompletionValue);
        habitChild.child("checked").setValue(isChecked);
        habitChild.child("dateLastChecked").setValue(today);
        dataChild.child("completions").setValue(newCompletionValue);
        dataChild.child("checked").setValue(isChecked);
        dataChild.child("dateLastChecked").setValue(today);

    }

    //return true if the streak does not need to be reset
    public boolean validateStreak(String habitId, List<Integer> repeatDays, String lastChecked){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        List<DatabaseReference> userHabitChildren = new ArrayList<>();
        for(Integer val : repeatDays){
            String day = Integer.toString(val);
            userHabitChildren.add(mDatabase.child("userhabits").child(userId).child(day).child(habitId));
        }
        DatabaseReference habitChild = mDatabase.child("habits").child(habitId);

        String [] splitter = lastChecked.split("-");
        Integer year = Integer.parseInt(splitter[0]);
        Integer month = Integer.parseInt(splitter[1]);
        Integer day = Integer.parseInt(splitter[2]);
        LocalDate dateLastChecked = LocalDate.of(year,month,day);

        boolean streakIsValid = dateLastCheckedIsValid(dateLastChecked, repeatDays);

        if(streakIsValid){
            return true;
        }

        resetStreak(habitId, repeatDays);
        return false;

    }

    private Boolean dateLastCheckedIsValid(LocalDate dateLastChecked, List<Integer> repeatDays){
        LocalDate now = LocalDate.now();
        LocalDate max = LocalDate.of(1900, 1, 1);
        for(Integer val: repeatDays) {
            LocalDate compareDate = now.with(TemporalAdjusters.previous(DayOfWeek.of(val)));
            if (compareDate.isAfter(max)) {
                max = compareDate;
            }
        }
        if(max.isEqual(dateLastChecked)) {
            return true;
        }
        return false;
    }

    private void resetStreak(String habitId, List<Integer> repeatDays) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        List<DatabaseReference> userHabitChildren = new ArrayList<>();
        for (Integer val : repeatDays) {
            String day = Integer.toString(val);
            userHabitChildren.add(mDatabase.child("userhabits").child(userId).child(day).child(habitId));
        }
        DatabaseReference habitChild = mDatabase.child("habits").child(habitId);

        updateStreak(0, userHabitChildren, habitChild);
    }


    
}