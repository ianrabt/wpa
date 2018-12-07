package net.ianrabt.wpa.views;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.Query;

import net.ianrabt.wpa.FBDataDelegate;
import net.ianrabt.wpa.FBRepository;
import net.ianrabt.wpa.R;
import net.ianrabt.wpa.controllers.DataVisController;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DataVisFragment extends Fragment implements FBDataDelegate {
    DataVisController controller;
    PieChart chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_vis, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        controller = new DataVisController(this);
        chart = (PieChart) getView().findViewById(R.id.chart);
    }

    @Override
    public void handleData(double completionPercentage) {
        PieDataSet set = createDataSet("Habit Completion Percentage", completionPercentage);
        set.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(set);
        chart.setData(data);
    }

    private PieDataSet createDataSet(String label, double completionPercentage) {
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry((float) completionPercentage, "Completed"));
        entries.add(new PieEntry((float) (1 - completionPercentage), "Skipped"));

        return new PieDataSet(entries, label);
    }

    @Override
    public void render() {
        chart.invalidate();
    }
}
