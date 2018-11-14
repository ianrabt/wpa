package net.ianrabt.wpa.models;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HabitModel {
    private String uid;
    private String author;
    private String habitId;
    private String name;
    private int streakCounter;
    private int completions;
    private List<Integer> repeatsOnDays;
    private String time;
    private boolean checked;
    private double lat;
    private double lon;



    public HabitModel(){
        //Default constructor required for call to DataSnapshot.getValue(Post.Class)
    }


    public HabitModel(String habitId, String uid, String author, String habitName, List<Integer> repeatsOnDays, String time, Double lat, Double lon) {
        this.uid = uid;
        this.author = author;
        this.habitId = habitId;
        this.name = habitName;
        this.streakCounter = 0;
        this.completions = 0;
        this.repeatsOnDays = repeatsOnDays;
        this.time = time;
        this.checked = false;
        if (lat != null & lon != null){
            this.lat = lat;
            this.lon = lon;
        }
    }

    public HabitCellModel convertToHabitCellModel(HabitModel model){
        return new HabitCellModel(model.name, new Date());
    }

    // Getter Methods
    public String getAuthor() {
        return this.author;
    }

    public String getUid() {
        return this.uid;
    }

    public String getHabitId() { return habitId; }

    public String getName() {
        return this.name;
    }

    public int getStreakCounter() {
        return streakCounter;
    }

    public int getCompletions() { return completions; }

    public List<Integer> getRepeatsOnDays() { return repeatsOnDays; }

    public String getTime() { return time; }

    public boolean isChecked() {
        return checked;
    }

    public double getLat() { return lat; }

    public double getLon() { return lon; }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("name", name);
        result.put("streakCounter", streakCounter);
        result.put("completions", completions);
        result.put("repeatsOnDays", repeatsOnDays);
        result.put("time", time);
        result.put("checked", checked);
        result.put("lat",lat);
        result.put("lon", lon);

        return result;
    }

}