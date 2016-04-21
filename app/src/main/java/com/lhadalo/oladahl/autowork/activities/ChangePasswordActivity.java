package com.lhadalo.oladahl.autowork.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.fragments.ChangePasswordFragment;



public class ChangePasswordActivity extends AppCompatActivity implements ChangePasswordFragment.OnFragmentInteraction {
    private ChangePasswordFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initFragment();
    }



    private void initFragment() {
        fragment = new ChangePasswordFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_Change_password, fragment).commit();
    }


    @Override
    public void onClickBtnChangePassword(String newPassword, String newPasswordConfirmation, String oldPassword) {

    }
}
