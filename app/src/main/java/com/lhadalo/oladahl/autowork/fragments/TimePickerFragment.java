package com.lhadalo.oladahl.autowork.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * Created by oladahl on 16-04-27.
 */
public class TimePickerFragment extends DialogFragment{
    private TimePickerDialog.OnTimeSetListener listener;

    public static TimePickerFragment newInstance() {
        return new TimePickerFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (TimePickerDialog.OnTimeSetListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int hour = 12; //TODO Ändra så tiden ändras i relation till start och sluttid.
        int minute = 0;

        return new TimePickerDialog(getActivity(), listener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}

