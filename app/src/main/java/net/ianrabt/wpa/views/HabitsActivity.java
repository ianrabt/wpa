package net.ianrabt.wpa.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import net.ianrabt.wpa.FBRepository;
import net.ianrabt.wpa.FBRepositoryDelegate;
import net.ianrabt.wpa.HabitItemAdapter;
import net.ianrabt.wpa.R;
import net.ianrabt.wpa.controllers.HabitsController;
import net.ianrabt.wpa.models.HabitCellModel;
import net.ianrabt.wpa.models.HabitModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HabitsActivity extends AppCompatActivity implements FBRepositoryDelegate, View.OnClickListener {
    FirebaseAuth mAuth;
    private FBRepository mRepository;
    private Toolbar mTopToolbar;
    FirebaseAuth.AuthStateListener mAuthListener;

    RecyclerView recyclerView;
    private TextView dayTextView;
    private TextView emptyView;
    HabitItemAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    HabitsController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(HabitsActivity.this, LoginActivity.class));
                }
            }
        };

        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);
        setContentView(R.layout.activity_habits);

        controller = new HabitsController(this);
        controller.queryHabits();
    }

    @Override
    public void handleHabitResponse(ArrayList<HabitModel> habitResponse) {
        controller.handleHabitResponse(habitResponse);
    }

    public void render() {
        emptyView = (TextView) findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new HabitItemAdapter(controller.habitsList, controller);
        recyclerView.setAdapter(adapter);

        String dayLongName = controller.getDay();
        recyclerView.addItemDecoration(new HeaderViewDecoration(this,
                recyclerView,  R.layout.habit_header, dayLongName));

        if (controller.habitsList.isEmpty()){
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mAuth.signOut();
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.create:
                controller.segueToCreateHabitActivity();
                break  ;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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

