package net.ianrabt.wpa;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import net.ianrabt.wpa.models.HabitCellModel;

import java.util.ArrayList;

public class HabitItemAdapter extends RecyclerView.Adapter<HabitItemAdapter.MyViewHolder> {
    private ArrayList<HabitCellModel> mDataset; // hold the habits data from db

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CheckBox mCheckBox;
        public TextView mHabitName;
        public TextView mHabitTime;
        public TextView mStreak;


        public MyViewHolder(View itemView){
            super(itemView);

            mCheckBox = itemView.findViewById(R.id.checkbox);
            mHabitName = itemView.findViewById(R.id.habitName);
            mHabitTime = itemView.findViewById(R.id.habitTimeRange);
            mStreak = itemView.findViewById(R.id.streakCounter);

        }

    }


    // Provide a suitable constructor
    public HabitItemAdapter(ArrayList<HabitCellModel> myDataset) {
        mDataset = myDataset;
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

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position]);
        // holder is the UI element, position relates to the element in the list of tasks in the recycler view

//        holder.mCheckBox
        holder.mHabitName.setText((CharSequence) mDataset.get(position).getHabitName());
        String habitTime = mDataset.get(position).getHour() + ":" + mDataset.get(position).getMinute();
        holder.mHabitTime.setText(habitTime);
        holder.mStreak.setText(String.valueOf(mDataset.get(position).getStreakCounter()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer pos = (Integer) holder.mCheckBox.getTag();
                String checkedHabitId = mDataset.get(pos).getHabitId();
                // TODO: call incrementStreak in FBrepository.java
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}



