package net.ianrabt.wpa.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.ianrabt.wpa.R;
import net.ianrabt.wpa.models.CalendarsModel;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        CalendarsModel model = new CalendarsModel(getApplicationContext());
    }
}
