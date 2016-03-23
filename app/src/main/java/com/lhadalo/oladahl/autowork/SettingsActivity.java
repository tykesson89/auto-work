package com.lhadalo.oladahl.autowork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import UserPackage.User;

public class SettingsActivity extends AppCompatActivity {
        private Button btnChangeUserInfo;
        private EditText etFirstName, etLastName, etEmail, etOldPassword, etNewPassword;
        private TextView tvDeleteAccount;
        private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Componentsinit();
        ListenersInit();

    }

    public void Componentsinit(){
        btnChangeUserInfo = (Button)findViewById(R.id.btnChangeUserInfo);
        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etLastName = (EditText)findViewById(R.id.etLastName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etNewPassword = (EditText)findViewById(R.id.etNewPassword);
        etOldPassword = (EditText)findViewById(R.id.etOldPassword);
        tvDeleteAccount = (TextView)findViewById(R.id.tvDeleteAccount);
    }

    public void ListenersInit(){
        btnChangeUserInfo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                User user = new User(getFirstName(), getLastName(), getEmail(), getNewPassword(), userId, getOldPassword());
                new SettingsController(SettingsActivity.this, "Change User Info", user);
            }
        });

        tvDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(getFirstName(), getLastName(), getEmail(), getNewPassword(), userId);
                new SettingsController(SettingsActivity.this, "Delete User", user);

            }
        });
    }

    public String getFirstName(){
        return etFirstName.getText().toString();
    }
    public String getLastName(){
        return etLastName.getText().toString();
    }
    public String getEmail(){
        return etEmail.getText().toString();
    }
    public String getNewPassword(){
        return etNewPassword.getText().toString();
    }
    public String getOldPassword(){
        return etOldPassword.getText().toString();
    }
}
