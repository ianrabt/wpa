package net.ianrabt.wpa.views;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;

import net.ianrabt.wpa.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DataVisFragment extends Fragment {

    public DataVisFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_vis, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        PieChart chart = (PieChart) getView().findViewById(R.id.chart);
        PieData data;

    }
}
