package net.ianrabt.wpa.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.ianrabt.wpa.FBRepository;
import net.ianrabt.wpa.FBRepositoryDelegate;
import net.ianrabt.wpa.HabitItemAdapter;
import net.ianrabt.wpa.R;
import net.ianrabt.wpa.models.HabitCellModel;
import net.ianrabt.wpa.models.HabitModel;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.ui.PlacePicker;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HabitsActivity extends AppCompatActivity implements FBRepositoryDelegate, View.OnClickListener {

    int PLACE_PICKER_REQUEST = 1;
    RecyclerView recyclerView;
    private TextView dayTextView;
    private TextView emptyView;
    private Calendar sCalendar = Calendar.getInstance();
    ArrayList<HabitCellModel> habitsList = new ArrayList<HabitCellModel>();
    HabitItemAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FBRepository mRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);
        Button addHabit = findViewById(R.id.add_button);
        addHabit.setOnClickListener(this);

        mRepository = new FBRepository(this);

        String day = Integer.toString(sCalendar.get(Calendar.DAY_OF_WEEK));
        mRepository.getHabitsByDay(day);

    }

    @Override
    public void handleHabitResponse(ArrayList<HabitModel> habitResponse) {
        for (int i = 0; i < habitResponse.size(); i++) {
            habitsList.add(new HabitCellModel(habitResponse.get(i)));
        }
    }

    public void render() {
        emptyView = (TextView) findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new HabitItemAdapter(habitsList);
        recyclerView.setAdapter(adapter);

        String dayLongName = sCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        recyclerView.addItemDecoration(new HeaderViewDecoration(this,
                recyclerView,  R.layout.habit_header, dayLongName));

        if (habitsList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }


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
            case R.id.add_button:
                Intent newActivity = new Intent(this, CreateHabitActivity.class);
                startActivity(newActivity);
                break;
        }
    }


//    public void onCheckboxClicked(View view) {
//        boolean checked = ((CheckBox) view).isChecked();
//        mRepository.incrementStreak();
//        // Update streak counter
//        if (checked) {
//            // TODO: send this information back to the database
//            // TODO: update the UI
//        }
//
//    }
}

