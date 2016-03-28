package com.lhadalo.oladahl.autowork;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created on 2015-11-26 at 21:32.
 */
public class DatePickerFragment extends DialogFragment{
	DatePickerDialog.OnDateSetListener listener;

	public DatePickerFragment(DatePickerDialog.OnDateSetListener listener){
		this.listener = listener;
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
