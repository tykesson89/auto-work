package com.lhadalo.oladahl.autowork.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lhadalo.oladahl.autowork.R;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddWorkpassFragment extends Fragment {
    private OnFragmentInteraction callback;
    private EventListener listener = new EventListener();
    private LinearLayout layoutWorkplace;
    private LinearLayout layoutStart;
    private LinearLayout layoutStop;
    private LinearLayout layoutBreak;
    private LinearLayout layoutAddNote;

    private EditText etTitle, etAddNote;

    private TextView txtWorkplace;
    private TextView txtDateStart;
    private TextView txtTimeStart;
    private TextView txtDateEnd;
    private TextView txtTimeEnd;
    private TextView txtBrake;


    public interface OnFragmentInteraction {
        void onClickWorkplace();

        void onClickDateStart();

        void onClickTimeStart();

        void onClickDateEnd();

        void onClickTimeEnd();

        void onClickBreak();

        void onClickAdd();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_workpass, container, false);

        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar_1);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.setTitle("Add workpass");

        etTitle = (EditText)view.findViewById(R.id.et_title);
        etAddNote = (EditText)view.findViewById(R.id.et_add_note);


        layoutWorkplace = (LinearLayout)view.findViewById(R.id.txt_layout_workplace);
        layoutStart = (LinearLayout)view.findViewById(R.id.txt_layout_start);
        layoutStop = (LinearLayout)view.findViewById(R.id.txt_layout_stop);
        layoutBreak = (LinearLayout)view.findViewById(R.id.txt_layout_lunch);
        layoutAddNote = (LinearLayout)view.findViewById(R.id.txt_layout_add_notes);

        txtWorkplace = (TextView)layoutWorkplace.getChildAt(1);
        txtDateStart = (TextView)layoutStart.getChildAt(1);
        txtTimeStart = (TextView)layoutStart.getChildAt(2);
        txtDateEnd = (TextView)layoutStop.getChildAt(1);
        txtTimeEnd = (TextView)layoutStop.getChildAt(2);
        txtBrake = (TextView)layoutBreak.getChildAt(1);



        txtWorkplace.setText("Workplace");
        txtBrake.setText("Add brake");

        initListeners();
    }

    private void initListeners() {
        txtWorkplace.setOnClickListener(listener);
        txtDateStart.setOnClickListener(listener);
        txtTimeStart.setOnClickListener(listener);
        txtDateEnd.setOnClickListener(listener);
        txtTimeEnd.setOnClickListener(listener);
        txtBrake.setOnClickListener(listener);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnFragmentInteraction)context;
        } catch (ClassCastException e) {
            Log.e("", context.getClass().getCanonicalName() +
                    " must implement OnFragmentInteraction");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_workpass, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_add){
            callback.onClickAdd();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTimeStartTag(GregorianCalendar date){
        txtTimeStart.setTag(date);
    }

    public GregorianCalendar getTimeStartTag(){
        return (GregorianCalendar)txtTimeStart.getTag();
    }

    public void setDateEndTag(GregorianCalendar date){
        txtDateEnd.setTag(date);
    }

    public GregorianCalendar getDateEndTag(){
        return (GregorianCalendar)txtDateEnd.getTag();
    }

    public void setTimeEndTag(GregorianCalendar date){
        txtTimeEnd.setTag(date);
    }

    public GregorianCalendar getTimeEndTag(){
        return (GregorianCalendar)txtTimeEnd.getTag();
    }

    public void setDateStartTag(GregorianCalendar date){
        txtDateStart.setTag(date);
    }

    public GregorianCalendar getDateStartTag(){
        return (GregorianCalendar)txtDateStart.getTag();
    }

    public void setTxtDateStart(String str) {
        txtDateStart.setText(str);
    }

    public void setTxtDateEnd(String str) {
        txtDateEnd.setText(str);
    }

    public void setTxtTimeStart(String str) {
        txtTimeStart.setText(str);
    }

    public void setTxtTimeEnd(String str) {
        txtTimeEnd.setText(str);
    }

    public void setTxtBrake(String str) {
        txtBrake.setText(str);
    }

    public String getTitle(){
        return etTitle.getText().toString();
    }

    public String getStartDate(){
        return txtDateStart.getText().toString();
    }

    public String getStartTime(){
        return txtTimeStart.getText().toString();
    }

    public String getEndDate(){
        return txtDateEnd.getText().toString();
    }

    public String getEndTime(){
        return txtTimeEnd.getText().toString();
    }

    public String getBrakeTime(){
        return txtBrake.getText().toString();
    }

    public String getNote(){
        return etAddNote.getText().toString();
    }

    private class EventListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.equals(txtWorkplace)) {
                callback.onClickWorkplace();
            } else if (view.equals(txtDateStart)) {
                callback.onClickDateStart();
            } else if (view.equals(txtTimeStart)) {
                callback.onClickTimeStart();
            } else if (view.equals(txtDateEnd)) {
                callback.onClickDateEnd();
            } else if (view.equals(txtTimeEnd)) {
                callback.onClickTimeEnd();
            } else if (view.equals(txtBrake)) {
                callback.onClickBreak();
            }
        }

    }
}
