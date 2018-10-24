package net.ianrabt.wpa.models;
import java.util.Date;

public class HabitModel {
    private String habitName;
    private int streakCounter;
    private Date creationDate;

    public HabitModel(String habitName) {
        this.habitName = habitName;
        this.streakCounter = 0;
        this.creationDate = new Date();
    }

    // Getter Methods
    public String getHabitName() {
        return this.habitName;
    }

    public int getStreakCounter() {
        return streakCounter;
    }

    public Date getCreationDate(){
        return creationDate;
    }
}
