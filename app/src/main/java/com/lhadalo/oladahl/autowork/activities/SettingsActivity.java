package com.lhadalo.oladahl.autowork.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import UserPackage.User;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.SettingsController;
import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity implements SettingsFragment.OnFragmentInteraction {
    private SettingsFragment fragment;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initFragment();
    }

    private void initFragment() {
        fragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_settings, fragment).commit();
    }

    @Override
    public void onClickBtnChangeUserInfo(String firstName, String lastName, String email,
                                         String oldPassword, String newPassword) {
        User user = new User(firstName, lastName, email, newPassword, userId, oldPassword);
        new SettingsController(SettingsActivity.this, Tag.CHANGE_USER_INFO, user);
    }

    @Override
    public void onClickDeleteAccount(String firstName, String lastName, String email,
                                     String oldPassword, String newPassword) {

        User user = new User(firstName, lastName, email, newPassword, userId, oldPassword);
        new SettingsController(SettingsActivity.this, Tag.DELETE_USER, user);
    }
}
