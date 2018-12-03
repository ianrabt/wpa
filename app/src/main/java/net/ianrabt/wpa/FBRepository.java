package net.ianrabt.wpa;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.ianrabt.wpa.models.HabitModel;
import net.ianrabt.wpa.views.CreateHabitActivity;
import net.ianrabt.wpa.views.HabitsActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FBRepository{

    private DatabaseReference mDatabase;
    private FBRepositoryDelegate delegate;

    public FBRepository() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void setDelegate(FBRepositoryDelegate delegate) {
        this.delegate = delegate;
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
                delegate.handleHabitResponse(habitList);
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
                delegate.handleHabitResponse(habitList);
                delegate.render();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //log
            }
        });


    }

    //if increment is true, then increment the streak counter, otherwise decrement the streak counter
    public void updateCounts(String habitId, Integer currentStreakValue, boolean increment,
                            List<Integer> repeatDays){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        List<DatabaseReference> userHabitChildren = new ArrayList<>();
        for(Integer val : repeatDays){
            String day = Integer.toString(val);
            userHabitChildren.add(mDatabase.child("userhabits").child(userId).child(day).child(habitId));
        }
        DatabaseReference habitChild = mDatabase.child("habits").child(habitId);
        DatabaseReference dataChild = mDatabase.child("data").child(userId).child(habitId);

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat spf= new SimpleDateFormat("yyyy-MM-dd");
        String date = spf.format(today);

        if(increment){
            updateCompletion(currentStreakValue+1, true, userHabitChildren,
                    habitChild, dataChild, date);
            updateStreak(currentStreakValue+1, userHabitChildren, habitChild);
        } else{
            updateCompletion(currentStreakValue-1, false, userHabitChildren,
                    habitChild, dataChild, date);
            updateStreak(currentStreakValue-1, userHabitChildren, habitChild);
        }


    }

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
        }
        dataChild.child("completions").setValue(newCompletionValue);
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
        LocalDate c = LocalDate.of(year,month,day);

        for(Integer val: repeatDays){
            LocalDate compareDate = c.with(TemporalAdjusters.previousOrSame(DayOfWeek.of(val)));
            if( c == compareDate){
                return true;
            }
        }
        //None of the previousDays dates matches with dateLastChecked so reset the streak
        resetStreak(habitId, repeatDays);
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