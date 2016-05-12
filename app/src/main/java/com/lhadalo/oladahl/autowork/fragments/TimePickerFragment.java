package com.lhadalo.oladahl.autowork.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;

import com.lhadalo.oladahl.autowork.Tag;

import java.util.Calendar;

/**
 * Created by oladahl on 16-04-27.
 */
public class TimePickerFragment extends DialogFragment{
    private TimePickerDialog.OnTimeSetListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (TimePickerDialog.OnTimeSetListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();


        return new TimePickerDialog(getActivity(), listener, args.getInt(Tag.HOUR), args.getInt(Tag.MINUTE),
                DateFormat.is24HourFormat(getActivity()));
    }
}

