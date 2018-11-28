package net.ianrabt.wpa.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import net.ianrabt.wpa.FBRepositoryDelegate;
import net.ianrabt.wpa.HabitItemAdapter;
import net.ianrabt.wpa.R;
import net.ianrabt.wpa.controllers.HabitsController;
import net.ianrabt.wpa.models.HabitModel;
import java.util.ArrayList;

public class HabitTodoFragment extends Fragment implements FBRepositoryDelegate, View.OnClickListener {

    RecyclerView recyclerView;
    private TextView dayTextView;
    private TextView emptyView;
    HabitItemAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    HabitsController controller;
    View activity_hab;
    View habit_todo;
    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_habits, container, false);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = new HabitsController(this);
        mContext = getActivity();
        controller.queryHabits();
    }

    @Override
    public void handleHabitResponse(ArrayList<HabitModel> habitResponse) {
        controller.handleHabitResponse(habitResponse);
    }

    public void render() {

        emptyView = (TextView) getView().findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) getView().findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new HabitItemAdapter(controller.habitsList, controller);
        recyclerView.setAdapter(adapter);

        String dayLongName = controller.getDay();
        recyclerView.addItemDecoration(new HeaderViewDecoration(mContext,
                recyclerView, R.layout.habit_header, dayLongName));

        if (controller.habitsList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        FloatingActionButton addHabit = (FloatingActionButton) getView().findViewById(R.id.create);
        recyclerView.setVisibility(View.VISIBLE);
        addHabit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.create:
                controller.segueToCreateHabitActivity();
                break  ;

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
