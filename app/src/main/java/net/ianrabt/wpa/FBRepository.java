package net.ianrabt.wpa;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.ianrabt.wpa.models.HabitModel;

import java.util.HashMap;
import java.util.Map;

public class FBRepository implements Repository {

   private DatabaseReference mDatabase;
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

   public FBRepository() {
       this.mDatabase = FirebaseDatabase.getInstance().getReference();
   }


   public void createRef() {
//       DatabaseReference myRef = mDatabase.getReference("message");
//       myRef.setValue("Hello, World!");
//       Log.d("Sent", "MEssage sent");
   }

   public void createHabit(String habitName){
       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       String id = user.getUid();
       String author = user.getDisplayName();
       String key = mDatabase.child("habits").push().getKey();
       HabitModel habit = new HabitModel(id, author, habitName);
       Map<String, Object> habitValues = habit.toMap();


       Map<String, Object>child = new HashMap<String, Object>();
       child.put("/habits/" + key, habitValues);
       child.put("/userhabits/" + id + "/" + key, habitValues);

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
               for (DataSnapshot habitSnapshot: dataSnapshot.getChildren()) {
                   HabitModel child = habitSnapshot.getValue(HabitModel.class);
                   Log.d("tag", String.valueOf(child));
               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
               //log
           }
       });
   }


}

