package com.lhadalo.oladahl.autowork;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddWorkpassFragment extends Fragment {
    private OnFragmentInteraction callback;
    private EventListener listener = new EventListener();
    private LinearLayout layoutWorkplace;
    private LinearLayout layoutStart;
    private LinearLayout layoutStop;
    private LinearLayout layoutBreak;
    private LinearLayout layoutAddNote;
    private TextView txtWorkplace;

    private TextView txtDateStart;
    private TextView txtTimeStart;

    private TextView txtDateEnd;
    private TextView txtTimeEnd;

    private TextView txtBreak;
    private TextView txtAddNote;

    interface OnFragmentInteraction {
        void onClickWorkplace();
        void onClickDateStart();
        void onClickTimeStart();
        void onClickDateEnd();
        void onClickTimeEnd();
        void onClickBreak();
        void onClickAddNote();
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

        layoutWorkplace = (LinearLayout) view.findViewById(R.id.txt_layout_workplace);
        layoutStart = (LinearLayout) view.findViewById(R.id.txt_layout_start);
        layoutStop = (LinearLayout) view.findViewById(R.id.txt_layout_stop);
        layoutBreak = (LinearLayout) view.findViewById(R.id.txt_layout_lunch);
        layoutAddNote = (LinearLayout) view.findViewById(R.id.txt_layout_add_notes);

        txtWorkplace = (TextView) layoutWorkplace.getChildAt(1);
        txtDateStart = (TextView) layoutStart.getChildAt(1);
        txtTimeStart = (TextView) layoutStart.getChildAt(2);

        txtDateEnd = (TextView) layoutStop.getChildAt(1);

        txtTimeEnd = (TextView) layoutStop.getChildAt(2);
        txtBreak = (TextView) layoutBreak.getChildAt(1);
        txtAddNote = (TextView) layoutAddNote.getChildAt(1);

        txtWorkplace.setText("Workplace");
        txtDateStart.setText("Sun, Mar 27, 2016");
        txtTimeStart.setText("15:15");

        txtDateEnd.setText("Sun, Mar 27, 2016");
        txtTimeEnd.setText("18:15");

        txtBreak.setText("Add break");
        txtAddNote.setText("Add note");

        initListeners();
    }

    private void initListeners() {
        txtWorkplace.setOnClickListener(listener);

        txtDateStart.setOnClickListener(listener);
        txtTimeStart.setOnClickListener(listener);

        txtDateEnd.setOnClickListener(listener);
        txtTimeEnd.setOnClickListener(listener);

        txtBreak.setOnClickListener(listener);
        txtAddNote.setOnClickListener(listener);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnFragmentInteraction) context;
        } catch (ClassCastException e) {
            Log.e("", context.getClass().getCanonicalName() +
                    " must implement OnFragmentInteraction");
        }
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

    private class EventListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view.equals(txtWorkplace)){
                callback.onClickWorkplace();
            }
            else if(view.equals(txtDateStart)){
                callback.onClickDateStart();
            }
            else if(view.equals(txtTimeStart)){
                callback.onClickTimeStart();
            }
            else if(view.equals(txtDateEnd)){
                callback.onClickDateEnd();
            }
            else if(view.equals(txtTimeEnd)){
                callback.onClickTimeEnd();
            }
            else if(view.equals(txtBreak)){
                callback.onClickBreak();
            }
            else if(view.equals(txtAddNote)){
                callback.onClickAddNote();
            }
        }

    }
}
