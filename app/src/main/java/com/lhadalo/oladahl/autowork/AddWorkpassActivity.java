package com.lhadalo.oladahl.autowork;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by oladahl on 16-03-26.
 */
public class AddWorkpassActivity extends AppCompatActivity
        implements AddWorkpassFragment.OnFragmentInteraction, DatePickerDialog.OnDateSetListener {
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
        dialogSource = Tag.START_DATE;
        DatePickerFragment dp = new DatePickerFragment();
        dp.show(getSupportFragmentManager(), "datepicker");
    }

    @Override
    public void onClickTimeStart() {

    }

    @Override
    public void onClickDateEnd() {
        dialogSource = Tag.END_DATE;
        DatePickerFragment dp = new DatePickerFragment();
        dp.show(getSupportFragmentManager(), "datepicker");
    }

    @Override
    public void onClickTimeEnd() {

    }

    @Override
    public void onClickBreak() {

    }

    @Override
    public void onClickAddNote() {

    }

    private String formatDate(int year, int month, int dayOfMonth) {
        switch (month) {
            case 0:
                return getResources().getStringArray(R.array.months)[0] + " " + dayOfMonth + ", " + year;
            case 1:
                return getResources().getStringArray(R.array.months)[1] + " " + dayOfMonth + ", " + year;
            case 2:
                return getResources().getStringArray(R.array.months)[2] + " " + dayOfMonth + ", " + year;
            case 3:
                return getResources().getStringArray(R.array.months)[3] + " " + dayOfMonth + ", " + year;
            case 4:
                return getResources().getStringArray(R.array.months)[4] + " " + dayOfMonth + ", " + year;
            case 5:
                return getResources().getStringArray(R.array.months)[5] + " " + dayOfMonth + ", " + year;
            case 6:
                return getResources().getStringArray(R.array.months)[6] + " " + dayOfMonth + ", " + year;
            case 7:
                return getResources().getStringArray(R.array.months)[7] + " " + dayOfMonth + ", " + year;
            case 8:
                return getResources().getStringArray(R.array.months)[8] + " " + dayOfMonth + ", " + year;
            case 9:
                return getResources().getStringArray(R.array.months)[9] + " " + dayOfMonth + ", " + year;
            case 10:
                return getResources().getStringArray(R.array.months)[10] + " " + dayOfMonth + ", " + year;
            case 11:
                return getResources().getStringArray(R.array.months)[11] + " " + dayOfMonth + ", " + year;
            default:
                return null;
        }
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if(dialogSource == Tag.START_DATE){
            fragment.setTxtDateStart(formatDate(year, month, day));
        }
        else if(dialogSource == Tag.END_DATE){
            fragment.setTxtDateEnd(formatDate(year, month, day));
        }
        Toast.makeText(this, String.valueOf(dialogSource), Toast.LENGTH_SHORT).show();

    }


    @SuppressLint("ValidFragment")
    public static class DatePickerFragment extends DialogFragment{
        private DatePickerDialog.OnDateSetListener listener;

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
