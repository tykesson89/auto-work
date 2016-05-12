package com.lhadalo.oladahl.autowork.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lhadalo.oladahl.autowork.R;

public class WorkpassViewerFragment extends Fragment {
    private LinearLayout layoutBreak;
    private LinearLayout layoutNote;

    private Toolbar toolbar;

    private TextView txtTitle;
    private TextView txtWorkplace;
    private TextView txtTime;
    private TextView txtSalary;
    private TextView txtBreak;
    private TextView txtNote;

    private ImageView imgWorkplace;
    private ImageView imgTime;
    private ImageView imgSalary;
    private ImageView imgBreak;
    private ImageView imgNote;

    private FloatingActionButton fab;


    private OnFragmentInteraction callback;

    public interface OnFragmentInteraction {
        void onClickFAB();
        void onClickDelete();

        void onClickClose();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_workpass_viewer, container, false);

        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        toolbar = (Toolbar)view.findViewById(R.id.toolbar_1);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_clear_white_24dp));
        toolbar.setNavigationContentDescription("Close Window");


        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        LinearLayout layoutWorkplace = (LinearLayout)view.findViewById(R.id.txt_layout_workplace);
        LinearLayout layoutTime = (LinearLayout)view.findViewById(R.id.txt_layout_time);
        LinearLayout layoutSalary = (LinearLayout)view.findViewById(R.id.txt_layout_salary);


        layoutBreak = (LinearLayout)view.findViewById(R.id.txt_layout_lunch);
        layoutNote = (LinearLayout)view.findViewById(R.id.txt_layout_note);


        txtTitle = (TextView)view.findViewById(R.id.txt_title);

        imgWorkplace = (ImageView)layoutWorkplace.getChildAt(0);
        txtWorkplace = (TextView)layoutWorkplace.getChildAt(1);

        imgTime = (ImageView)layoutTime.getChildAt(0);
        txtTime = (TextView)layoutTime.getChildAt(1);

        imgSalary = (ImageView)layoutSalary.getChildAt(0);
        txtSalary = (TextView)layoutSalary.getChildAt(1);

        imgBreak = (ImageView)layoutBreak.getChildAt(0);
        txtBreak = (TextView)layoutBreak.getChildAt(1);

        imgNote = (ImageView)layoutNote.getChildAt(0);
        txtNote = (TextView)layoutNote.getChildAt(1);

        fab = (FloatingActionButton)view.findViewById(R.id.fab);

        initListeners();
    }

    private void initListeners() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickClose();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickFAB();
            }
        });
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

    public void setLayoutBreakGone(){
        layoutBreak.setVisibility(View.GONE);
    }

    public void setLayoutNoteGone(){
        layoutNote.setVisibility(View.GONE);
    }

    public void setImgWorkplace(Drawable imgWorkplace) {
        this.imgWorkplace.setImageDrawable(imgWorkplace);
    }

    public void setImgTime(Drawable imgTime) {
        this.imgTime.setImageDrawable(imgTime);
    }

    public void setImgSalary(Drawable imgSalary) {
        this.imgSalary.setImageDrawable(imgSalary);
    }

    public void setImgBreak(Drawable imgBreak) {
        this.imgBreak.setImageDrawable(imgBreak);
    }

    public void setImgNote(Drawable imgNote) {
        this.imgNote.setImageDrawable(imgNote);
    }

    public void setTxtNote(String txtNote) {
        this.txtNote.setText(txtNote);
    }

    public void setTxtTitle(String txtTitle) {
        this.txtTitle.setText(txtTitle);
    }

    public void setTxtWorkplace(String txtWorkplace) {
        this.txtWorkplace .setText(txtWorkplace);
    }

    public void setTxtTime(String txtTime) {
        this.txtTime.setText(txtTime);
    }

    public void setTxtSalary(String txtSalary){
        this.txtSalary.setText(txtSalary);
    }

    public void setTxtBreak(String txtBreak) {
        this.txtBreak.setText(txtBreak);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_workpass_viewer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        callback.onClickDelete();
        return true;
    }
}
