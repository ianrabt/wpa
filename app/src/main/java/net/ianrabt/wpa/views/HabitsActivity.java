package net.ianrabt.wpa.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.ianrabt.wpa.R;

import java.util.ArrayList;
import java.util.List;

public class HabitsActivity extends AppCompatActivity {

    ListView listView;
    List habitsList = new ArrayList();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);

        listView = (ListView)findViewById(R.id.list_view);

//        habitsList.add("item 1");
//        habitsList.add("item 2");
//        habitsList.add("item 3");
//        habitsList.add("item 4");
//        habitsList.add("item 5");
//        habitsList.add("item 6");
//        habitsList.add("item 7");
//        habitsList.add("item 8");
//        habitsList.add("item 9");
//        habitsList.add("item 10");
//        habitsList.add("item 11");
//        habitsList.add("item 12");
//        habitsList.add("item 13");
//        habitsList.add("item 14");
//        habitsList.add("item 15");
//
//        adapter = new ArrayAdapter(HabitsActivity.this, android.R.layout.simple_list_item_1, habitsList);
        listView.setAdapter(adapter);

    }

    public void onCheckboxClicked(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());

        // Update streak counter

        // TODO: send this information back to the database
    }
}
