package net.ianrabt.wpa.controllers;

import android.content.Intent;

import net.ianrabt.wpa.FBRepository;
import net.ianrabt.wpa.Repository;
import net.ianrabt.wpa.models.HabitCellModel;
import net.ianrabt.wpa.models.HabitModel;
import net.ianrabt.wpa.views.CreateHabitActivity;
import net.ianrabt.wpa.views.HabitsActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HabitsController {

    public FBRepository mRepository;
    private Calendar sCalendar = Calendar.getInstance();
    public ArrayList<HabitCellModel> habitsList = new ArrayList<HabitCellModel>();
    private HabitsActivity activity;

    public HabitsController(HabitsActivity activity){
        this.activity = activity;
        mRepository = new FBRepository();
        mRepository.setDelegate(activity);

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
        Intent newActivity = new Intent(activity, CreateHabitActivity.class);
        activity.startActivity(newActivity);
    }

}