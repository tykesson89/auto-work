package com.lhadalo.oladahl.autowork;

import android.content.Context;
import android.content.Intent;
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

/**
 * Created by oladahl on 16-03-26.
 */
public class MainFragment extends Fragment {
    private OnFragmentInteraction callback;
    private Button btnSettings, btnLogOut;
    private TextView tvName;
    private Toolbar toolbar;

    interface OnFragmentInteraction{
        void onActionSettingsPressed();
        void onActionLogOutPressed();
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
        tvName = (TextView)view.findViewById(R.id.tvName);

        toolbar = (Toolbar)view.findViewById(R.id.toolbarMain);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);

        initListeners(view);
    }

    private void initListeners(View view){

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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.callback = (OnFragmentInteraction)context;
    }

    public void setTextTvName(String str){
        tvName.setText(str);
    }
}
