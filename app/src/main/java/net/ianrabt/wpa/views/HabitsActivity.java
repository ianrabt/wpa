package net.ianrabt.wpa.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import net.ianrabt.wpa.HabitItemAdapter;
import net.ianrabt.wpa.R;
import net.ianrabt.wpa.models.HabitCellModel;

import java.util.ArrayList;
import java.util.List;

public class HabitsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    HabitCellModel[] habitsList = new HabitCellModel[13];
    HabitItemAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView textFavorites;
    private TextView textSchedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);


        recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        // Hardcode a habit to test the list
        java.util.Date date=new java.util.Date();
        HabitCellModel habit1 = new HabitCellModel("workout", date);
        habitsList[0] = habit1;
        habitsList[1] = habit1;
        habitsList[2] = habit1;
        habitsList[3] = habit1;
        habitsList[4] = habit1;
        habitsList[5] = habit1;
        habitsList[6] = habit1;
        habitsList[7] = habit1;
        habitsList[8] = habit1;
        habitsList[9] = habit1;
        habitsList[10] = habit1;
        habitsList[11] = habit1;
        habitsList[12] = habit1;

        adapter = new HabitItemAdapter(habitsList);
        recyclerView.setAdapter(adapter);

        // start the bottom navigation menu
        textFavorites = (TextView) findViewById(R.id.text_favorites);
        textSchedules = (TextView) findViewById(R.id.text_schedules);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_favorites:
                                textFavorites.setVisibility(View.VISIBLE);
                                textSchedules.setVisibility(View.GONE);
                                break;
                            case R.id.action_schedules:
                                textFavorites.setVisibility(View.GONE);
                                textSchedules.setVisibility(View.VISIBLE);
                                break;
                        }
                        return false;
                    }
                });
    }

//    public void onCheckboxClicked(View view) {
//        boolean checked = ((CheckBox) view).isChecked();
//
//        // Update streak counter
//        if (checked) {
//            // TODO: send this information back to the database
//            // TODO: update the UI
//        }
//
//    }
}
