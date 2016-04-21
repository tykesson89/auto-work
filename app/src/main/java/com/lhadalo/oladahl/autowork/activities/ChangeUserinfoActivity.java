package com.lhadalo.oladahl.autowork.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.fragments.ChangeUserinfoFragment;


public class ChangeUserinfoActivity extends AppCompatActivity implements ChangeUserinfoFragment.OnFragmentInteraction {
        private ChangeUserinfoFragment fragment;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_change_password);
            initFragment();
        }


        private void initFragment() {
            fragment = new ChangeUserinfoFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_Change_Userinfo, fragment).commit();
        }


    @Override
    public void onClickBtnChangeUserinfo(String firstname, String Lastname, String email, String password) {

    }
}
