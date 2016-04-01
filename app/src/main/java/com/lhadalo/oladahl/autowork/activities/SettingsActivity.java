package com.lhadalo.oladahl.autowork.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import UserPackage.User;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.SQLiteDB;
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
        SQLiteDB sqLiteDB = new SQLiteDB(this);
        userId = sqLiteDB.getUserId(this);
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
        new ChangeUserInfo().execute(user);
    }

    @Override
    public void onClickDeleteAccount() {

        User user = new User(userId);
        new DeleteUser().execute(user);
    }


    class DeleteUser extends AsyncTask<User, Void, String> {




        protected String doInBackground(User... params) {
            return null;
        }
    }




    class ChangeUserInfo extends AsyncTask<User, Void, String>{





        protected String doInBackground(User... params) {
            return null;
        }
    }
}
