package net.ianrabt.wpa.models;
import android.arch.lifecycle.ViewModel;
import java.util.Date;

public class HabitCellModel extends ViewModel {
    private String habitName;
    private int hour;
    private int minute;
    private boolean checked;
    private int streakCounter;


    public HabitCellModel(String habitName, Date habitDate) {
        this.habitName = habitName;
        this.streakCounter = 0;
    }

    public HabitCellModel(HabitModel model){
        this.habitName = model.getName();
        this.hour = model.getHour();
        this.minute = model.getMinute();
        this.checked = model.isChecked();
        this.streakCounter = model.getStreakCounter();
    }

    /* Getter Methods */
    public String getHabitName() {
        return this.habitName;
    }

    public int getHour() { return this.hour; }

    public int getMinute() { return minute; }

    public boolean isChecked() { return checked; }

    public int getStreakCounter() { return streakCounter; }

}
