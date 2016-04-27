package com.lhadalo.oladahl.autowork.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by oladahl on 16-04-27.
 */
public class DatePickerFragment extends DialogFragment{
    private DatePickerDialog.OnDateSetListener listener;

    public static DatePickerFragment newInstance() {
        return new DatePickerFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DatePickerDialog.OnDateSetListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }
}

