package net.ianrabt.wpa.views;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import net.ianrabt.wpa.FBRepository;
import net.ianrabt.wpa.R;
import net.ianrabt.wpa.Repository;
import net.ianrabt.wpa.controllers.CreateHabitController;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateHabitActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView timeText;
    public TextView habitNameText;
    CreateHabitController controller;
    public CheckBox monday;
    public CheckBox sunday;
    public CheckBox tuesday;
    public CheckBox wednesday;
    public CheckBox thursday;
    public CheckBox friday;
    public CheckBox saturday;
    private Double lat;
    private Double lon;
    LatLng latlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);
        controller = new CreateHabitController(this);
        setUIComponents();
    }

    public void setUIComponents() {
        setTextViews();
        setButtons();
        setCheckBoxes();
        controller.addDays();
    }


    public void setTextViews(){
        timeText = (TextView) findViewById(R.id.time_text);
        habitNameText = findViewById(R.id.habit_name_text);
        timeText.setOnClickListener(this);
        habitNameText.setOnClickListener(this);
    }

    public void setButtons(){
        Button create = (Button) findViewById(R.id.create);
        Button addLocation = (Button) findViewById(R.id.add_location);
        addLocation.setOnClickListener(this);
        create.setOnClickListener(this);
    }

    public void setCheckBoxes(){
        monday = (CheckBox) findViewById(R.id.monday);
        sunday = (CheckBox) findViewById(R.id.sunday);
        tuesday = (CheckBox) findViewById(R.id.tuesday);
        wednesday = (CheckBox) findViewById(R.id.wednesday);
        thursday = (CheckBox) findViewById(R.id.thursday);
        friday = (CheckBox) findViewById(R.id.friday);
        saturday = (CheckBox) findViewById(R.id.saturday);
        monday.setOnClickListener(this);
        tuesday.setOnClickListener(this);
        wednesday.setOnClickListener(this);
        thursday.setOnClickListener(this);
        friday.setOnClickListener(this);
        saturday.setOnClickListener(this);
        sunday.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.time_text:
                controller.pickTime(v);
                break;
            case R.id.create:
                controller.uploadHabit();
                break;
            case R.id.add_location:
                controller.showPlacePicker();
                break;
        }
    }
}
