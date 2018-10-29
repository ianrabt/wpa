package net.ianrabt.wpa;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import android.text.format.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ******************
 * to use this fragment
 *         DialogFragment newFragment = new TimePickerFragment();
 *         newFragment.show(getSupportFragmentManager(), "timePicker");
 * *******************
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        System.out.println(hourOfDay);
        int time = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

}