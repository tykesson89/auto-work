package com.lhadalo.oladahl.autowork;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by oladahl on 16-03-26.
 */
public class AddWorkpassActivity extends AppCompatActivity
        implements AddWorkpassFragment.OnFragmentInteraction, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    private AddWorkpassFragment fragment;
    private int dialogSource = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workpass);

        initFragment();
    }

    private void initFragment() {
        fragment = new AddWorkpassFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_add_workpass, fragment).commit();
    }

    @Override
    public void onClickWorkplace() {
        Toast.makeText(this, "Workplace", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickDateStart() {
        dialogSource = Tag.START_DATE_TIME;
        DatePickerFragment.newInstance().show(getSupportFragmentManager(), "datepicker");
    }

    @Override
    public void onClickTimeStart() {
        dialogSource = Tag.START_DATE_TIME;
        TimePickerFragment.newInstance().show(getSupportFragmentManager(), "timepicker");
    }

    @Override
    public void onClickDateEnd() {
        dialogSource = Tag.END_DATE_TIME;
        DatePickerFragment.newInstance().show(getSupportFragmentManager(), "datepicker");
    }

    @Override
    public void onClickTimeEnd() {
        dialogSource = Tag.END_DATE_TIME;
        TimePickerFragment.newInstance().show(getSupportFragmentManager(), "timepicker");
    }

    @Override
    public void onClickBreak() {

    }

    @Override
    public void onClickAddNote() {

    }

    private String formatDate(int year, int month, int dayOfMonth) {
        return getResources().getStringArray(R.array.months)[month] + " " + dayOfMonth + ", " + year;
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if(dialogSource == Tag.START_DATE_TIME){
            fragment.setTxtDateStart(formatDate(year, month, day));
        }
        else if(dialogSource == Tag.END_DATE_TIME){
            fragment.setTxtDateEnd(formatDate(year, month, day));
        }
        Toast.makeText(this, String.valueOf(dialogSource), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        if(dialogSource == Tag.START_DATE_TIME){
            fragment.setTxtTimeStart(String.valueOf(DateFormat.format("kk:mm",
                    new GregorianCalendar(0, 0, 0, hour, minute))));
        }
        else if(dialogSource == Tag.END_DATE_TIME){
            fragment.setTxtTimeEnd(String.valueOf(DateFormat.format("kk:mm",
                    new GregorianCalendar(0,0,0,hour,minute))));
        }
    }

    public static class TimePickerFragment extends DialogFragment{
        private TimePickerDialog.OnTimeSetListener listener;

        static TimePickerFragment newInstance(){
            TimePickerFragment f = new TimePickerFragment();

            return f;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            listener = (TimePickerDialog.OnTimeSetListener)context;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), listener, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }
    }

    public static class DatePickerFragment extends DialogFragment{
        private DatePickerDialog.OnDateSetListener listener;


        static DatePickerFragment newInstance() {
            DatePickerFragment f = new DatePickerFragment();

            return f;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            listener = (DatePickerDialog.OnDateSetListener)context;
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
}
