package com.lhadalo.oladahl.autowork;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;
import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * Created on 2015-11-26 at 21:23.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);


		return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
	}

	@Override
	public void onTimeSet(TimePicker timePicker, int i, int i1) {

	}
}
