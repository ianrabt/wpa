package net.ianrabt.wpa.models;
import android.arch.lifecycle.ViewModel;
import java.util.Date;
import java.util.List;

public class HabitCellModel extends ViewModel {
    private String habitId;
    private String habitName;
    private String time;
    private boolean checked;
    private int streakCounter;
    private int completionCount;
    private String dateLastChecked;
    private String previousDateLastChecked;
    List<Integer> repeatDays;

    public HabitCellModel(HabitModel model){
        this.habitId = model.getKey();
        this.habitName = model.getName();
        this.time = model.getTime();
        this.checked = model.isChecked();
        this.streakCounter = model.getStreakCounter();
        this.dateLastChecked = model.getDateLastChecked();
        this.previousDateLastChecked = model.getPreviousDateLastChecked();
        this.repeatDays = model.getRepeatsOnDays();
        this.completionCount = model.getCompletions();
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

    public void setChecked(boolean checked) { this.checked = checked; }

    public void setStreakCounter(int streakCounter) { this.streakCounter = streakCounter;}

    public List<Integer> getRepeatDays() { return repeatDays; }

    public int getCompletionCount() { return completionCount; }

    public void setCompletionCount(int completionCount) { this.completionCount = completionCount; }

    public String getPreviousDateLastChecked() { return previousDateLastChecked; }

    public void setPreviousDateLastChecked(String date) { this.previousDateLastChecked = date; }


}
