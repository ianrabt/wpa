package net.ianrabt.wpa.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.ianrabt.wpa.FBRepository;
import net.ianrabt.wpa.FBRepositoryDelegate;
import net.ianrabt.wpa.HabitItemAdapter;
import net.ianrabt.wpa.R;
import net.ianrabt.wpa.models.HabitCellModel;
import net.ianrabt.wpa.models.HabitModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HabitsActivity extends AppCompatActivity implements FBRepositoryDelegate, View.OnClickListener {

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



        mRepository = new FBRepository(this, null);

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

        FloatingActionButton addHabit = (FloatingActionButton) findViewById(R.id.create);
        recyclerView.setVisibility(View.VISIBLE);
        addHabit.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.create:
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

