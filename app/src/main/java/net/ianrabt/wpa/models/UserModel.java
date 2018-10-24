package net.ianrabt.wpa.models;
import java.util.ArrayList;

public class UserModel {
    private String username;
    private ArrayList<HabitModel> habits;

    public UserModel(String username) {
        this.username = username;
        this.habits = new ArrayList<HabitModel>();
    }

    public void addHabit(HabitModel habit){
        this.habits.add(habit);
    }

    /* Getter Methods */
    public String getUsername() {
        return this.username;
    }

    public ArrayList<HabitModel> getHabits() {
        return this.habits;
    }
}
