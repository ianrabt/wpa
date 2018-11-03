package net.ianrabt.wpa;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.Calendar;
import java.util.Date;

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
import net.ianrabt.wpa.views.HomeActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FBRepository{

   private DatabaseReference mDatabase;
   private HomeActivity mHome;
   private ChildEventListener mChildEventListener = new ChildEventListener() {

       @Override
       public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
           Object habit = dataSnapshot.getValue(HabitModel.class);
           System.out.println(habit);
           Log.d("tag", (String) habit);
       }

       @Override
       public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Object habit = dataSnapshot.getValue();
       }

       @Override
       public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            Object habit = dataSnapshot.getKey();
       }

       @Override
       public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Object habit = dataSnapshot.getValue();

       }

       @Override
       public void onCancelled(@NonNull DatabaseError databaseError) {
            //chill
       }
   };

   public FBRepository(HomeActivity home) {
       this.mHome = home;

       this.mDatabase = FirebaseDatabase.getInstance().getReference();
   }



   public void createHabit(String habitName, List<Integer> repeatsOnDays, int hour, int minute){
       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       String id = user.getUid();
       String author = user.getDisplayName();
       String key = mDatabase.child("habits").push().getKey();
       HabitModel habit = new HabitModel(id, author, habitName, repeatsOnDays, hour, minute);
       Map<String, Object> habitValues = habit.toMap();


       Map<String, Object>child = new HashMap<String, Object>();
       child.put("/habits/" + key, habitValues);

       //update the days for which these habits go under
       List<Integer> days = habit.getRepeatsOnDays();
       for (int i = 0; i < habit.getRepeatsOnDays().size(); ++i){
           child.put("/userhabits/" + days.get(i) + "/" + id + "/" + key, habitValues);
       }


       mDatabase.updateChildren(child);
       //mDatabase.child("habit").child("name" ).setValue(habit);
   }

   public void getHabits(){
       //mDatabase.addChildEventListener(mChildEventListener);
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
               mHome.handleHabitResponse(habitList);
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
                mHome.handleHabitResponse(habitList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //log
            }
        });


    }

   public void incrementStreak(String habitId, Integer currentStreakValue){
       //TODO: Read currentStreak from cell and increment
       int currentStreak = 0;
       FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
       String userId = currentUser.getUid();
       mDatabase.child("userhabits").child(userId).child("-LPnqJwY8RtTG2Mi_b8h").child("streak_counter").setValue(1);
       mDatabase.child("habits").child("-LPnqJwY8RtTG2Mi_b8h").child("streak_counter").setValue(1);

       Date currentTime = Calendar.getInstance().getTime();
   }



}

