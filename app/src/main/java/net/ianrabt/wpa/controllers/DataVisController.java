package net.ianrabt.wpa.controllers;

import android.provider.ContactsContract;

import net.ianrabt.wpa.FBRepository;
import net.ianrabt.wpa.models.HabitCellModel;
import net.ianrabt.wpa.views.DataVisFragment;
import net.ianrabt.wpa.views.HabitTodoFragment;
import net.ianrabt.wpa.views.HabitsActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class DataVisController {
    private FBRepository mRepository;
    private Calendar calendar = Calendar.getInstance();
    private DataVisFragment fragment;
    private HabitsActivity activity;

    public DataVisController(DataVisFragment fragment) {
        this.fragment = fragment;
        mRepository = new FBRepository();
        mRepository.setDataDelegate(fragment);
        mRepository.getVisualizationData();
    }
}
