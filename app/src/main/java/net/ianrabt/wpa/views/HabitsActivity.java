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
import android.widget.ListView;
import android.widget.TextView;

import net.ianrabt.wpa.HabitItemAdapter;
import net.ianrabt.wpa.R;
import net.ianrabt.wpa.models.HabitCellModel;

import java.util.ArrayList;
import java.util.List;

public class HabitsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    HabitCellModel[] habitsList = new HabitCellModel[2];
    HabitItemAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);




        recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        // Hardcode a habit to test the list
        java.util.Date date=new java.util.Date();
        HabitCellModel habit1 = new HabitCellModel("workout", date);
        habitsList[0] = habit1;
        habitsList[1] = habit1;

        adapter = new HabitItemAdapter(habitsList);

        // TODO: make an adapter for this recycler view
        recyclerView.setAdapter(adapter);

    }

//    public void onCheckboxClicked(View view) {
////        View parent = (View) view.getParent();
////        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
////        String task = String.valueOf(taskTextView.getText());
//
//        // Update streak counter
//
//        // TODO: send this information back to the database
//    }
}
