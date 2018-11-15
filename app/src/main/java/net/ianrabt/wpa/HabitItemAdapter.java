package net.ianrabt.wpa;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import net.ianrabt.wpa.models.HabitCellModel;

import java.util.ArrayList;

public class HabitItemAdapter extends RecyclerView.Adapter<HabitItemAdapter.MyViewHolder> {
    private ArrayList<HabitCellModel> mDataset; // hold the habits data from db
    private FBRepository mRepository;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder


    // Provide a suitable constructor
    public HabitItemAdapter(ArrayList<HabitCellModel> myDataset, FBRepository myRepository) {
        mDataset = myDataset;
        mRepository = myRepository;
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
    public void onBindViewHolder( MyViewHolder holder, final int position) {
//        // holder is the UI element, position relates to the element in the list of tasks in the recycler view
//        // **** Currently causes program to crash ****
        System.out.println("first string printed");
        holder.mHabitName.setText((CharSequence) mDataset.get(position).getHabitName());

        // Build the string to represent the habit time
        // TODO: implement time range
        holder.mHabitTime.setText(mDataset.get(position).getTime());
        holder.mStreak.setText(String.valueOf(mDataset.get(position).getStreakCounter()));

//        holder.mCheckBox.setTag(position);
        System.out.println("after setting tag");

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
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
//            mCheckBox.setOnClickListener(this);

        }


        private OnClickListener checkboxOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("on bind click");
                Integer position = (Integer) mCheckBox.getTag();
                String checkedHabitId = mDataset.get(position).getHabitId();
                Integer beforeClickStreakNum = mDataset.get(position).getStreakCounter();
                mRepository.incrementStreak(checkedHabitId, beforeClickStreakNum);
            }
        };

//        private void onCheckboxClicked(View view) {
//            CheckBox check = this.mCheckBox;
//            Integer position = (Integer) view.check.getTag();
//            String checkedHabitId = mDataset.get(position).getHabitId();
//            Integer beforeClickStreakNum = mDataset.get(position).getStreakCounter();
//            mRepository.incrementStreak(checkedHabitId, beforeClickStreakNum);
//        }
    }


}



