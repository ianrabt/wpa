package net.ianrabt.wpa.models;
import android.arch.lifecycle.ViewModel;
import java.util.Date;

public class HabitCellModel extends ViewModel {
    private String habitId;
    private String habitName;
    private String time;
    private boolean checked;
    private int streakCounter;
    private String dateLastChecked;

    public HabitCellModel(HabitModel model){
        this.habitId = model.getKey();
        this.habitName = model.getName();
        this.time = model.getTime();
        this.checked = model.isChecked();
        this.streakCounter = model.getStreakCounter();
        this.dateLastChecked = model.getDateLastChecked();
    }

    /* Getter Methods */
    public String getHabitId() {return this.habitId; }

    public String getHabitName() {
        return this.habitName;
    }

    public String getTime() { return this.time; }

    public boolean isChecked() { return checked; }

    public int getStreakCounter() { return streakCounter; }

    public String getDateLastChecked() { return dateLastChecked; }
}
