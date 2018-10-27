package net.ianrabt.wpa.models;
import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HabitModel {
    private String name;
    private int streakCounter;
    private Date creationDate;
    private String uid;
    private String author;


    public HabitModel(){
        //Default constructor required for call to DataSnapshot.getValue(Post.Class)
    }


    public HabitModel(String uid, String author, String habitName) {
        this.uid = uid;
        this.author = author;
        this.name = habitName;
        this.streakCounter = 0;
        this.creationDate = new Date();
    }

    // Getter Methods
    public String getName() {
        return this.name;
    }

    public int getStreakCounter() {
        return streakCounter;
    }

    public Date getCreationDate(){
        return creationDate;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getUid() {
        return this.uid;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("name", name);
        result.put("streak_counter", streakCounter);
        //result.put()

        return result;
     }

}
