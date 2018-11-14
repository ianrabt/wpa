package net.ianrabt.wpa.controllers;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import net.ianrabt.wpa.FBRepository;
import net.ianrabt.wpa.views.CreateHabitActivity;
import net.ianrabt.wpa.views.HabitsActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateHabitController {

    CreateHabitActivity activity;
    FBRepository mRepository;
    private ArrayList<CheckBox> days = new ArrayList<>();
    int PLACE_PICKER_REQUEST = 1;
    private Double lat;
    private Double lon;
    LatLng latlng;

    public CreateHabitController(CreateHabitActivity activity){
        this.activity = activity;
        this.mRepository = new FBRepository();
    }

    public void uploadHabit() {
        String name = activity.habitNameText.getText().toString();
        ArrayList<Integer> days = getDays();
        String time = activity.timeText.getText().toString();
        mRepository.createHabit(name,days,time, lat, lon);
        Intent newActivity = new Intent(activity, HabitsActivity.class);
        activity.startActivity(newActivity);
    }

    private ArrayList<Integer> getDays(){
        ArrayList<Integer> list = new ArrayList<>();
        for(CheckBox item : days){
            if (item.isChecked()){
                list.add(dayToIntMapper(item.getText().toString()));
            }
        }
        return list;
    }

    private static int dayToIntMapper(String day){
        switch (day){
            case "Monday":
                return 2;
            case "Tuesday":
                return 3;
            case "Wednesday":
                return 4;
            case "Thursday":
                return 5;
            case "Friday":
                return 6;
            case "Saturday":
                return 7;
            case "Sunday":
                return 1;
        }
        return 0;
    }

    public void addDays(){
        days.add(activity.monday);
        days.add(activity.tuesday);
        days.add(activity.wednesday);
        days.add(activity.thursday);
        days.add(activity.friday);
        days.add(activity.saturday);
        days.add(activity.sunday);
    }


    public void showPlacePicker(){

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            activity.startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST);
        } catch (Exception e){
            e.printStackTrace();
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, activity);
                LatLng coord = place.getLatLng();
                lat = coord.latitude;
                lon = coord.longitude;
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(activity, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void pickTime(View view){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;

        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                activity.timeText.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

}
