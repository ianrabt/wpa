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

import net.ianrabt.wpa.FBRepository;
import net.ianrabt.wpa.FBRepositoryDelegate;
import net.ianrabt.wpa.R;
import net.ianrabt.wpa.models.HabitModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DataVisFragment extends Fragment implements FBRepositoryDelegate {

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

        PieDataSet set = createDataSet("Pie Chart"); // TODO come up with better name
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        // TODO add more styling options

        PieData data = new PieData(set);
        chart.setData(data);
        chart.invalidate();

    }

    private PieDataSet createDataSet(String label) {
        List<PieEntry> entries = new ArrayList<>();

        FBRepository repo = new FBRepository();

        repo.getHabits();

        entries.add(new PieEntry(30f, "Thirty Percent"));
        entries.add(new PieEntry(50f, "Seventy Percent yoooo"));
        entries.add(new PieEntry(20f, "nahhh this is twenty"));
        // ...
        // TODO use actual data

        return new PieDataSet(entries, label);
    }

    @Override
    public void handleHabitResponse(ArrayList<HabitModel> habitResponse) {
        //TODO WHAT DOES THIS DO????
    }

    @Override
    public void render() {
        //TODO WHAT DOES THIS DO????
    }
}
