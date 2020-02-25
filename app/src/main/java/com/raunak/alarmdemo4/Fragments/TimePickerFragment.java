package com.raunak.alarmdemo4.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.raunak.alarmdemo4.R;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment{
    //Fragments need to empty constructor
    TimePickerFragment(){
    }

    TimePickerFragment(TimePickerDialog.OnTimeSetListener onTimeSetListener){
        this.onTimeSetListener = onTimeSetListener;
    }
    TimePickerDialog.OnTimeSetListener onTimeSetListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), R.style.TimePickerTheme,onTimeSetListener,hour,minute, DateFormat.is24HourFormat(getActivity()));
    }
}