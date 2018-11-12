package net.ianrabt.wpa.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import net.ianrabt.wpa.FBRepository;
import net.ianrabt.wpa.FBRepositoryDelegate;
import net.ianrabt.wpa.HabitItemAdapter;
import net.ianrabt.wpa.R;
import net.ianrabt.wpa.models.HabitCellModel;
import net.ianrabt.wpa.models.HabitModel;

import java.util.ArrayList;
import java.util.List;

public class HabitsActivity extends AppCompatActivity implements FBRepositoryDelegate {

    RecyclerView recyclerView;
    ArrayList<HabitCellModel> habitsList = new ArrayList<HabitCellModel>();
    HabitItemAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FBRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);

        mRepository = new FBRepository(this);

        // Create habits to use for testing
        List<Integer> repeatDays = new ArrayList<>(2);
        repeatDays.add(1);
        repeatDays.add(2);

        mRepository.createHabit("workout", repeatDays, 1, 15);
        mRepository.createHabit("drink water", repeatDays, 1, 55);
        mRepository.createHabit("sleep 8 hrs", repeatDays, 2, 30);

        mRepository.getHabitsByDay("1");

    }

    @Override
    public void handleHabitResponse(ArrayList<HabitModel> habitResponse) {
        for (int i = 0; i < habitResponse.size(); i++) {
            habitsList.add(new HabitCellModel(habitResponse.get(i)));
        }
    }

    public void render() {
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new HabitItemAdapter(habitsList);
        recyclerView.setAdapter(adapter);
    }


    public void onCheckboxClicked(View view) {
//        boolean checked = ((CheckBox) view).isChecked();
//        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        Integer habitID = view.getId();
        // the id is the same when different checkboxes are clicked...

            // TODO: get the habit ID... map the checkbox id to the particular habit id
//        adapter = new HabitItemAdapter(habitsList);
//        recyclerView.setAdapter(adapter);
//        int adapterPosition = recyclerView.getAdapterPosition();
//        if (items.get(adapterPosition).getChecked()) {
//            mCheckedTextView.setChecked(false);
//            items.get(adapterPosition).setChecked(false);
//        }
//        else {
//            mCheckedTextView.setChecked(true);
//            items.get(adapterPosition).setChecked(true);
//        }
////        mRepository.incrementStreak(habitID, 8);
//        // Update streak counter
//        if (checked) {
//            // TODO: send this information back to the database
//            // TODO: update the UI
//    }

    }
}

