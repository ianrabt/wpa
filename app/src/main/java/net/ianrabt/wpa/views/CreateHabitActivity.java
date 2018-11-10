package net.ianrabt.wpa.views;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import net.ianrabt.wpa.FBRepository;
import net.ianrabt.wpa.R;
import net.ianrabt.wpa.Repository;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateHabitActivity extends AppCompatActivity implements View.OnClickListener {

    TextView timeText;
    TextView habitNameText;
//    CheckBox monday;
//    CheckBox sunday;
//    CheckBox tuesday;
//    CheckBox wednesday;
//    CheckBox thursday;
//    CheckBox friday;
//    CheckBox saturday;
    private ArrayList<CheckBox> days = new ArrayList<>();
    FBRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);
        timeText = (TextView) findViewById(R.id.time_text);
        habitNameText = findViewById(R.id.habit_name_text);
        timeText.setOnClickListener(this);
        habitNameText.setOnClickListener(this);
        Button create = (Button) findViewById(R.id.create);
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
        mRepository = new FBRepository(null, this);
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

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.time_text:
                pickTime(v);
                break;
            case R.id.create:
                uploadHabit();
                break;
        }
    }
}
