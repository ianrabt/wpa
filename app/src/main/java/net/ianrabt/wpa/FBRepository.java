package net.ianrabt.wpa;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FBRepository implements Repository {

   private DatabaseReference mDatabase;

   public FBRepository() {
       this.mDatabase = FirebaseDatabase.getInstance().getReference();
   }


   public void createRef() {
//       DatabaseReference myRef = mDatabase.getReference("message");
//       myRef.setValue("Hello, World!");
//       Log.d("Sent", "MEssage sent");
   }

   public void createHabit(){
       String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
       String key = mDatabase.child("habits").push().getKey();
       Map<String, Object> habit = new HashMap<String, Object>();
       habit.put("Name", "NewHabit");


       Map<String, Object>child = new HashMap<String, Object>();
       child.put("/habits/" + key, habit);
       child.put("/userhabits/" + id + "/" + key, habit);

       mDatabase.updateChildren(child);
       //mDatabase.child("habit").child("name" ).setValue(habit);
   }
}

