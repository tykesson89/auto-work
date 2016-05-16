package com.lhadalo.oladahl.autowork;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.lhadalo.oladahl.autowork.activities.MainActivity;
import com.lhadalo.oladahl.autowork.database.FetchWorkpasses;

import java.util.Calendar;

/**
 * Created by oladahl on 16-05-15.
 */
public class SpinnerListener implements AdapterView.OnItemSelectedListener {
    private MainActivity parent;

    public static SpinnerListener newInstance(Context context) {

        return new SpinnerListener(context);
    }

    private SpinnerListener(Context context) {
        this.parent = (MainActivity)context;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch(position) {
            case Calendar.JANUARY: FetchWorkpasses.newInstance(parent, Tag.ON_UPDATE_LIST).execute(Calendar.JANUARY);
                break;
            case Calendar.FEBRUARY: FetchWorkpasses.newInstance(parent, Tag.ON_UPDATE_LIST).execute(Calendar.FEBRUARY);
                break;
            case Calendar.MARCH: FetchWorkpasses.newInstance(parent, Tag.ON_UPDATE_LIST).execute(Calendar.MARCH);
                break;
            case Calendar.APRIL: FetchWorkpasses.newInstance(parent, Tag.ON_UPDATE_LIST).execute(Calendar.APRIL);
                break;
            case Calendar.MAY: FetchWorkpasses.newInstance(parent, Tag.ON_UPDATE_LIST).execute(Calendar.MAY);
                break;
            case Calendar.JUNE: FetchWorkpasses.newInstance(parent, Tag.ON_UPDATE_LIST).execute(Calendar.JUNE);
                break;
            case Calendar.JULY: FetchWorkpasses.newInstance(parent, Tag.ON_UPDATE_LIST).execute(Calendar.JULY);
                break;
            case Calendar.AUGUST: FetchWorkpasses.newInstance(parent, Tag.ON_UPDATE_LIST).execute(Calendar.AUGUST);
                break;
            case Calendar.SEPTEMBER: FetchWorkpasses.newInstance(parent, Tag.ON_UPDATE_LIST).execute(Calendar.SEPTEMBER);
                break;
            case Calendar.OCTOBER: FetchWorkpasses.newInstance(parent, Tag.ON_UPDATE_LIST).execute(Calendar.OCTOBER);
                break;
            case Calendar.NOVEMBER: FetchWorkpasses.newInstance(parent, Tag.ON_UPDATE_LIST).execute(Calendar.NOVEMBER);
                break;
            case Calendar.DECEMBER: FetchWorkpasses.newInstance(parent, Tag.ON_UPDATE_LIST).execute(Calendar.DECEMBER);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
