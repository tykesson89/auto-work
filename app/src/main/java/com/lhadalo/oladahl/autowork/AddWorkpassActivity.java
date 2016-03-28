package com.lhadalo.oladahl.autowork;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by oladahl on 16-03-26.
 */
public class AddWorkpassActivity extends AppCompatActivity implements AddWorkpassFragment.OnFragmentInteraction{
    private AddWorkpassFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workpass);

        initFragment();
    }

    private void initFragment(){
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

    }

    @Override
    public void onClickTimeStart() {

    }

    @Override
    public void onClickDateEnd() {

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

    
}
