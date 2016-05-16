package com.lhadalo.oladahl.autowork;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;

import com.lhadalo.oladahl.autowork.fragments.MainFragmentNew;

/**
 * Created by oladahl on 16-05-12.
 */
public class TestActivity extends AppCompatActivity implements MainFragmentNew.OnFragmentInteraction{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        MainFragmentNew fragment = new MainFragmentNew();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
    }

    @Override
    public void onFABPressed() {

    }

    @Override
    public void setCoordinatorLayout(CoordinatorLayout layout) {

    }
}
