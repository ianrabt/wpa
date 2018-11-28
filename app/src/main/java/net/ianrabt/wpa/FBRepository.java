package net.ianrabt.wpa;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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
import java.util.HashMap;
import java.util.List;
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


        Map<String, Object>child = new HashMap<String, Object>();
        child.put("/habits/" + key, habitValues);

        //update the days for which these habits go under
        List<Integer> days = habit.getRepeatsOnDays();
        for (int i = 0; i < habit.getRepeatsOnDays().size(); ++i){
            child.put("/userhabits/" + days.get(i) + "/" + id + "/" + key, habitValues);
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
        Query query = mDatabase.child("userhabits").child(day).child(userId);


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
                delegate.render();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //log
            }
        });


    }

    public void incrementStreak(String habitId, Integer currentStreakValue){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        int newStreakValue = currentStreakValue+1;
        String userId = currentUser.getUid();
        mDatabase.child("userhabits").child(userId).child(habitId).child("streak_counter").setValue(newStreakValue);
        mDatabase.child("habits").child(habitId).child("streak_counter").setValue(newStreakValue);

    }
}