package net.ianrabt.wpa.models;
import android.arch.lifecycle.ViewModel;
import java.util.Date;

public class HabitCellModel extends ViewModel {
    private String habitName;
    private Date habitDate;
    private int streakCounter;


    public HabitCellModel(String habitName, Date habitDate) {
        this.habitName = habitName;
        this.habitDate = habitDate;
        this.streakCounter = 0;
    }

    /* Getter Methods */
    public String getHabitName() {
        return this.habitName;
    }

    public Date getHabitDate(){
        return this.habitDate;
    }

    public int getStreakCounter() {
        return streakCounter;
    }

}
