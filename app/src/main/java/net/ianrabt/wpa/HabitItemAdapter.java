package net.ianrabt.wpa;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.ianrabt.wpa.models.HabitCellModel;

import java.util.ArrayList;
import java.util.List;

public class HabitItemAdapter extends ArrayAdapter<HabitCellModel>{
    private Context mContext;
    private List<HabitCellModel> habitsList = new ArrayList<>();

    public HabitItemAdapter(@NonNull Context context, ArrayList<HabitCellModel> list) {
        super(context, 0 , list);
        mContext = context;
        habitsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        //TODO: write this method for the habitcellmodel
//        if(listItem == null)
//            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);

//        Movie currentMovie = moviesList.get(position);

//        ImageView image = (ImageView)listItem.findViewById(R.id.imageView_poster);
//        image.setImageResource(currentMovie.getmImageDrawable());

//        TextView name = (TextView) listItem.findViewById(R.id.textView_name);
////        name.setText(currentMovie.getmName());
//
//        TextView release = (TextView) listItem.findViewById(R.id.textView_release);
////        release.setText(currentMovie.getmRelease());

//        return listItem;
    }
}

