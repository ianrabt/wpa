package net.ianrabt.wpa.controllers;

import android.content.Intent;

import net.ianrabt.wpa.FBRepository;
import net.ianrabt.wpa.Repository;
import net.ianrabt.wpa.models.HabitCellModel;
import net.ianrabt.wpa.models.HabitModel;
import net.ianrabt.wpa.views.CreateHabitActivity;
import net.ianrabt.wpa.views.HabitTodoFragment;
import net.ianrabt.wpa.views.HabitsActivity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HabitsController {

    private FBRepository mRepository;
    private LocalDate now = LocalDate.now();
    public ArrayList<HabitCellModel> habitsList = new ArrayList<HabitCellModel>();
    private HabitTodoFragment fragment;
    private HabitsActivity activity;

    public HabitsController(HabitTodoFragment fragment){
        this.fragment = fragment;
        mRepository = new FBRepository();
        mRepository.setDelegate(fragment);

    }

    public void queryHabits(){
        String day = Integer.toString(now.getDayOfWeek().getValue());
        mRepository.getHabitsByDay(day);
    }

    public void handleHabitResponse(ArrayList<HabitModel> habitResponse) {
        for (int i = 0; i < habitResponse.size(); i++) {
            habitsList.add(new HabitCellModel(habitResponse.get(i)));
        }
    }

    public String getDay(){
        return now.getDayOfWeek().name();
    }

    public void segueToCreateHabitActivity(){
        Intent newActivity = new Intent(fragment.getActivity(), CreateHabitActivity.class);
        fragment.getActivity().startActivity(newActivity);
    }

    public void updateCounts(String habitId, Integer currentStreakValue,
                             Integer currentCompletionValue, boolean increment,
                             List<Integer> repeatDays, String date, String newPreviousDate){
        mRepository.updateCounts(habitId, currentStreakValue, currentCompletionValue,
                increment, repeatDays, date, newPreviousDate);
    }

    public boolean validateStreak(String habitId, List<Integer> repeatDays, String dateLastChecked){
        return mRepository.validateStreak(habitId, repeatDays, dateLastChecked);
    }

}