package net.ianrabt.wpa;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import net.ianrabt.wpa.models.HabitCellModel;
import net.ianrabt.wpa.controllers.HabitsController;
import net.ianrabt.wpa.models.HabitModel;
import net.ianrabt.wpa.views.HabitsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HabitItemAdapter extends RecyclerView.Adapter<HabitItemAdapter.MyViewHolder> {
    private ArrayList<HabitCellModel> mDataset;
    private HabitsController mController;

    public HabitItemAdapter(ArrayList<HabitCellModel> myDataset, HabitsController myController) {
        mDataset = myDataset;
        mController = myController;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HabitItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.habit_todo, parent, false);
        MyViewHolder vh = new MyViewHolder(itemView);
        return vh;

    }

    public String getHabitId(int position){
        return mDataset.get(position).getHabitId();
    }
    public Integer getStreak(int position){
        return mDataset.get(position).getStreakCounter();
    }

    public boolean isChecked(int position) { return mDataset.get(position).isChecked(); }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder( MyViewHolder holder, final int position) {
        // holder is the UI element, position relates to the element in the list of tasks in the recycler view
        HabitCellModel cell = mDataset.get(position);
        holder.mHabitName.setText((CharSequence) cell.getHabitName());
        holder.mHabitTime.setText(cell.getTime());
        holder.mStreak.setText(String.valueOf(cell.getStreakCounter()));
        holder.mPosition = position;
        if(getCurrentDay().compareTo(cell.getDateLastChecked()) == 0){
            boolean isChecked = cell.isChecked();
            holder.mCheckBox.setChecked(isChecked);
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CheckBox mCheckBox;
        public TextView mHabitName;
        public TextView mHabitTime;
        public TextView mStreak;
        public int mPosition;

        public MyViewHolder(View itemView) {
            super(itemView);

            mCheckBox = itemView.findViewById(R.id.checkbox);
            mHabitName = itemView.findViewById(R.id.habitName);
            mHabitTime = itemView.findViewById(R.id.habitTimeRange);
            mStreak = itemView.findViewById(R.id.streakCounter);
            mCheckBox.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            Integer position = (Integer) mPosition;
            String checkedHabitId = getHabitId(position);
            Integer streakNum = getStreak(position);
            boolean isChecked = isChecked(position);
            int newStreak = streakNum + 1;
            mStreak.setText(Integer.toString(newStreak));
            mController.incrementStreak(checkedHabitId, streakNum, isChecked);
        }
    }

    public static String getCurrentDay() {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat spf= new SimpleDateFormat("yyyyMMdd");
        String date = spf.format(today);
        return date;
    }


}



