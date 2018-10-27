package net.ianrabt.wpa;

import net.ianrabt.wpa.models.HabitModel;

import java.util.ArrayList;

public interface Repository {

    public void createHabit();
    public void getHabits();
    public void incrementStreak(String habitId, Integer currentStreakValue);
}
