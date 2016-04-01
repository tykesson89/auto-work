package com.lhadalo.oladahl.autowork.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.WorkpassDBHelper;
import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.WorkpassModel;
import com.lhadalo.oladahl.autowork.fragments.AddWorkpassFragment;

/**
 * Created by oladahl on 16-03-26.
 */
public class AddWorkpassActivity extends AppCompatActivity
        implements AddWorkpassFragment.OnFragmentInteraction, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    private WorkpassDBHelper database = new WorkpassDBHelper(this);
    private AddWorkpassFragment fragment;
    private int dialogSource = 0;
    private String brakeTime;
    private List<WorkpassModel> workpassModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workpass);

        initFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        fragment.setTxtDateStart(formatDate(year, month, dayOfMonth));
        fragment.setDateStartTag(new GregorianCalendar(year, month, dayOfMonth));

        GregorianCalendar startTime = new GregorianCalendar(0,0,0,hourOfDay,minute);
        fragment.setTxtTimeStart(String.valueOf(DateFormat.format("kk:mm", startTime)));
        fragment.setTimeStartTag(startTime);

        fragment.setTxtDateEnd(formatDate(year, month, dayOfMonth));
        fragment.setDateEndTag(new GregorianCalendar(year, month, dayOfMonth));

        GregorianCalendar endTime = new GregorianCalendar(0,0,0, hourOfDay + 3, minute);
        fragment.setTxtTimeEnd(String.valueOf(DateFormat.format("kk:mm", endTime)));
        fragment.setTimeEndTag(endTime);

    }

    private void initFragment() {
        fragment = new AddWorkpassFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_add_workpass, fragment).commit();
    }

    @Override
    public void onClickWorkplace() {

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

        workpassModels = database.getAllWorkpasses();

        WorkpassModel m = workpassModels.get(0);

        Toast.makeText(this, m.getTitle(), Toast.LENGTH_SHORT).show();
        //createAlertDialog();
    }

    @Override
    public void onClickAdd() {

        WorkpassModel model = new WorkpassModel();
        model.setTitle(fragment.getTitle());
        model.setCompany(null);
        Timestamp dateTimeStart = formatDateTime(fragment.getDateStartTag(),
                fragment.getTimeStartTag());
        Timestamp dateTimeEnd = formatDateTime(fragment.getDateEndTag(),
                fragment.getTimeEndTag());
        model.setStartDateTime(dateTimeStart);
        model.setEndDateTime(dateTimeEnd);
        model.setBreaktime(Integer.parseInt(fragment.getBrakeTime()));
//        model.setNote(fragment.getNote());
//
//        GregorianCalendar date = fragment.getTimeStartTag();
//        date.get(Calendar.HOUR_OF_DAY);
//        String log = String.valueOf(date.get(Calendar.HOUR_OF_DAY));
//        Log.v(Tag.LOGTAG, log);
    }

    private Timestamp formatDateTime(GregorianCalendar date, GregorianCalendar time){
        return new Timestamp(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH), time.get(Calendar.HOUR_OF_DAY),
                time.get(Calendar.MINUTE), 0, 0);
    }

    private String formatDate(int year, int month, int dayOfMonth) {
        return getResources().getStringArray(R.array.months)[month] + " " + dayOfMonth + ", " + year;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if(dialogSource == Tag.START_DATE_TIME){
            fragment.setTxtDateStart(formatDate(year, month, day));
            fragment.setDateStartTag(new GregorianCalendar(year, month, day));
        }
        else if(dialogSource == Tag.END_DATE_TIME){
            fragment.setTxtDateEnd(formatDate(year, month, day));
            fragment.setDateStartTag(new GregorianCalendar(year, month, day));
        }

        Toast.makeText(this, String.valueOf(dialogSource), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        if(dialogSource == Tag.START_DATE_TIME){
            fragment.setTxtTimeStart(String.valueOf(DateFormat.format("kk:mm",
                    new GregorianCalendar(0, 0, 0, hour, minute))));
            fragment.setTimeStartTag(new GregorianCalendar(0,0,0,hour, minute));
        }
        else if(dialogSource == Tag.END_DATE_TIME){
            fragment.setTxtTimeEnd(String.valueOf(DateFormat.format("kk:mm",
                    new GregorianCalendar(0,0,0,hour,minute))));
            fragment.setTimeEndTag(new GregorianCalendar(0,0,0, hour, minute));

        }
    }

    private void createAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add brake in minutes");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                brakeTime = input.getText().toString();
                fragment.setTxtBrake(String.valueOf(brakeTime + " min"));
            }
        }
        ).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });

        builder.show();

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
