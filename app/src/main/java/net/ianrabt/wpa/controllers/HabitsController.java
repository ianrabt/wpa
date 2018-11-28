package net.ianrabt.wpa.controllers;

import android.content.Intent;

import net.ianrabt.wpa.FBRepository;
import net.ianrabt.wpa.Repository;
import net.ianrabt.wpa.models.HabitCellModel;
import net.ianrabt.wpa.models.HabitModel;
import net.ianrabt.wpa.views.CreateHabitActivity;
import net.ianrabt.wpa.views.HabitTodoFragment;
import net.ianrabt.wpa.views.HabitsActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HabitsController {

    private FBRepository mRepository;
    private Calendar sCalendar = Calendar.getInstance();
    public ArrayList<HabitCellModel> habitsList = new ArrayList<HabitCellModel>();
    private HabitTodoFragment fragment;
    private HabitsActivity activity;

    public HabitsController(HabitTodoFragment fragment){
        this.fragment = fragment;
        mRepository = new FBRepository();
        mRepository.setDelegate(fragment);

    }

    public void queryHabits(){
        String day = Integer.toString(sCalendar.get(Calendar.DAY_OF_WEEK));
        mRepository.getHabitsByDay(day);
    }

    public void handleHabitResponse(ArrayList<HabitModel> habitResponse) {
        for (int i = 0; i < habitResponse.size(); i++) {
            habitsList.add(new HabitCellModel(habitResponse.get(i)));
        }
    }

    public String getDay(){
        return sCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
    }

    public void segueToCreateHabitActivity(){
        Intent newActivity = new Intent(fragment.getActivity(), CreateHabitActivity.class);
        fragment.getActivity().startActivity(newActivity);
    }

    public void updateStreak(String habitId, Integer currentStreakValue, boolean increment){
        String day = Integer.toString(sCalendar.get(Calendar.DAY_OF_WEEK));
        mRepository.updateStreak(habitId, currentStreakValue, day, increment);
    }

}