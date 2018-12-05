package net.ianrabt.wpa;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
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
import java.util.Map;

public class FBRepository{

    private DatabaseReference mDatabase;
    private FBHabitDelegate delegate;

    public FBRepository() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void setDelegate(FBHabitDelegate delegate) {
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
                    List<Integer> days = habitSnapshot.child("repeats_on_days").getValue(t);
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

    public void getVisualizationData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        Query query = mDatabase.child("data").child(userId);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numOccurances = 0;
                int numCompletions = 0;
                for (DataSnapshot dataVisSnapshot : dataSnapshot.getChildren()) {
                    numCompletions += dataVisSnapshot.child("completions").getValue(Integer.class);
                    Long dateCreated = dataVisSnapshot.child("dataCreated").getValue(Long.class);
                    Integer daysPerWeek = dataVisSnapshot.child("daysPerWeek").getValue(Interger.class);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //log
            }
        });
    }

    //if increment is true, then increment the streak counter, otherwise decrement the streak counter
    public void updateStreak(String habitId, Integer currentStreakValue, String day, boolean increment){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        DatabaseReference userHabitChild = mDatabase.child("userhabits").child(userId).child(day).child(habitId);
        DatabaseReference habitChild = mDatabase.child("habits").child(habitId);
        DatabaseReference dataChild = mDatabase.child("data").child(userId).child(habitId);

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat spf= new SimpleDateFormat("yyyyMMdd");
        String date = spf.format(today);

        int newStreakValue;
        if(increment){
            newStreakValue = currentStreakValue+1;
            userHabitChild.child("checked").setValue(true);
        } else{
            newStreakValue = currentStreakValue-1;
            userHabitChild.child("checked").setValue(false);
        }
        userHabitChild.child("streakCounter").setValue(newStreakValue);
        userHabitChild.child("dateLastChecked").setValue(date);
        habitChild.child("streakCounter").setValue(newStreakValue);
        dataChild.child("completions").setValue(newStreakValue);

    }

    
}