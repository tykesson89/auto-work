package com.lhadalo.oladahl.autowork.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lhadalo.oladahl.autowork.R;

/**
 * Created by oladahl on 16-03-26.
 */
public class MainFragment extends Fragment {
    private OnFragmentInteraction callback;
    private TextView tvSalary,tvSalaryPass,tvNextPass,tvHours,tvHoursPass;
    private Toolbar toolbar;
    private Button btnTry;

    public interface OnFragmentInteraction{
        void onActionSettingsPressed();
        void onActionLogOutPressed();
        void onActionAddWorkpassPressed();
        void onButtonClickTry();
        void onActionLaunchTestActivityPressed();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); //För att berätta att det finns en meny
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initComponents(view);
        return view;
    }

    private void initComponents(View view){
        tvSalary = (TextView)view.findViewById(R.id.tvSalary);
        tvSalaryPass = (TextView)view.findViewById(R.id.tvSalaryPass);
        tvNextPass = (TextView)view.findViewById(R.id.tvNextPass);
        tvHours = (TextView)view.findViewById(R.id.tvHours);
        tvHoursPass = (TextView)view.findViewById(R.id.tvHoursPass);
        btnTry=(Button)view.findViewById(R.id.button_addCompany_try);

        toolbar = (Toolbar)view.findViewById(R.id.toolbar_main);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);

        initListeners();
    }

    private void initListeners(){

        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onButtonClickTry();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                callback.onActionSettingsPressed();
                break;

            case R.id.action_log_out:
                callback.onActionLogOutPressed();
                break;
            case R.id.action_add_workpass:
                callback.onActionAddWorkpassPressed();
                break;
            case R.id.action_launch_test:
                callback.onActionLaunchTestActivityPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.callback = (OnFragmentInteraction)context;
    }

    public void setTextTvSalary(String str){

        tvSalary.setText(str);
    }
    public void setTextTvSalaryPass(String str){

        tvSalaryPass.setText(str);
    }
    public void setTextTvNextPass(String str){

        tvNextPass.setText(str);
    }
    public void setTextTvHours(String str){

        tvHours.setText(str);
    }
    public void setTextTvHoursPass(String str){

        tvHoursPass.setText(str);
    }
}
