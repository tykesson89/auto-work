package com.lhadalo.oladahl.autowork.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.lhadalo.oladahl.autowork.Tag;

import java.util.Calendar;

/**
 * Created by oladahl on 16-04-27.
 */
public class DatePickerFragment extends DialogFragment{
    private DatePickerDialog.OnDateSetListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DatePickerDialog.OnDateSetListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();


        return new DatePickerDialog(getActivity(),
                listener, args.getInt(Tag.YEAR), args.getInt(Tag.MONTH), args.getInt(Tag.DAY));
    }
}

