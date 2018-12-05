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
import java.util.List;

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
    public List<Integer> getRepeatDays(int position) { return mDataset.get(position).getRepeatDays(); }
    public boolean isChecked(int position) { return mDataset.get(position).isChecked(); }
    public void setChecked(int position, boolean isChecked){ mDataset.get(position).setChecked(isChecked); }
    public void setStreakValue(int position, int value){ mDataset.get(position).setStreakCounter(value); }
    public String getDateLastChecked(int position) { return mDataset.get(position).getDateLastChecked();}
    public String getPreviousDateLastChecked(int position) { return mDataset.get(position).getPreviousDateLastChecked(); }
    public void setPreviousDateLastChecked(int position, String date) { mDataset.get(position).setPreviousDateLastChecked(date); }
    public Integer getCompletionValue(int position) { return mDataset.get(position).getCompletionCount(); }
    public void setCompletionValue(int position, int value) { mDataset.get(position).setCompletionCount(value);}
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder( MyViewHolder holder, final int position) {
        // holder is the UI element, position relates to the element in the list of tasks in the recycler view
        HabitCellModel cell = mDataset.get(position);
        holder.mHabitName.setText((CharSequence) cell.getHabitName());
        holder.mHabitTime.setText(cell.getTime());
        holder.mStreak.setText(String.valueOf(cell.getStreakCounter()));
        holder.mPosition = position;
        List<Integer> i = getRepeatDays(position);
        if(getCurrentDay().compareTo(cell.getDateLastChecked()) == 0){
            boolean isChecked = cell.isChecked();
            holder.mCheckBox.setChecked(isChecked);
        } else {
            boolean isValid = mController.validateStreak(cell.getHabitId(), cell.getRepeatDays(),
                    cell.getDateLastChecked());
            setChecked(position, false);
            if(!isValid){
                holder.mStreak.setText("0");
                setStreakValue(position, 0);
            }
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
            Integer completionValue = getCompletionValue(position);
            boolean isChecked = isChecked(position); // I want to know if this is the state before click or not...
            List<Integer> repeatDays = getRepeatDays(position);
            String date = getPreviousDateLastChecked(position);
            if (!isChecked){
                int newStreak = streakNum + 1;
                mStreak.setText(Integer.toString(newStreak));
                setChecked(position, true);
                setStreakValue(position,newStreak);
                mController.updateCounts(checkedHabitId, streakNum, completionValue,true, repeatDays, "");
                setCompletionValue(position, completionValue+1);
            }
            else if (isChecked){
                int newStreak = streakNum - 1;
                int newCompletionValue = completionValue - 1;
                mStreak.setText(Integer.toString(newStreak));
                setStreakValue(position, newStreak);
                setChecked(position,false);
                mController.updateCounts(checkedHabitId, streakNum, completionValue, false, repeatDays, date);
                setCompletionValue(position, newCompletionValue);
            }
        }
    }

    public static String getCurrentDay() {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat spf= new SimpleDateFormat("yyyy-MM-dd");
        String date = spf.format(today);
        return date;
    }


}



