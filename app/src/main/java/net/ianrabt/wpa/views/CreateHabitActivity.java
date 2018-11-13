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

import net.ianrabt.wpa.FBRepository;
import net.ianrabt.wpa.R;
import net.ianrabt.wpa.Repository;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateHabitActivity extends AppCompatActivity implements View.OnClickListener {

    TextView timeText;
    TextView habitNameText;
    private ArrayList<CheckBox> days = new ArrayList<>();
    FBRepository mRepository;
    int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);
        timeText = (TextView) findViewById(R.id.time_text);
        habitNameText = findViewById(R.id.habit_name_text);
        timeText.setOnClickListener(this);
        habitNameText.setOnClickListener(this);
        Button create = (Button) findViewById(R.id.create);
        Button addLocation = (Button) findViewById(R.id.add_location);
        addLocation.setOnClickListener(this);
        create.setOnClickListener(this);
        CheckBox monday = (CheckBox) findViewById(R.id.monday);
        CheckBox sunday = (CheckBox) findViewById(R.id.sunday);
        CheckBox tuesday = (CheckBox) findViewById(R.id.tuesday);
        CheckBox wednesday = (CheckBox) findViewById(R.id.wednesday);
        CheckBox thursday = (CheckBox) findViewById(R.id.thursday);
        CheckBox friday = (CheckBox) findViewById(R.id.friday);
        CheckBox saturday = (CheckBox) findViewById(R.id.saturday);
        days.add(monday);
        days.add(tuesday);
        days.add(wednesday);
        days.add(thursday);
        days.add(friday);
        days.add(saturday);
        days.add(sunday);
        monday.setOnClickListener(this);
        tuesday.setOnClickListener(this);
        wednesday.setOnClickListener(this);
        thursday.setOnClickListener(this);
        friday.setOnClickListener(this);
        saturday.setOnClickListener(this);
        sunday.setOnClickListener(this);
        mRepository = new FBRepository();
    }


    public void pickTime(View view){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;

        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeText.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    private void uploadHabit() {
        String name = habitNameText.getText().toString();
        ArrayList<Integer> days = getDays();
        String time = timeText.getText().toString();
        mRepository.createHabit(name,days,time);
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

    private void showPlacePicker(){

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (Exception e){
            e.printStackTrace();
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.time_text:
                pickTime(v);
                break;
            case R.id.create:
                uploadHabit();
                Intent newActivity = new Intent(this, HabitsActivity.class);
                startActivity(newActivity);
                break;
            case R.id.add_location:
                showPlacePicker();
                break;
        }
    }
}
