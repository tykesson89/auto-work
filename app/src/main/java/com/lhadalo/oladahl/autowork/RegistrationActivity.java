package com.lhadalo.oladahl.autowork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

import Networking.CreateUser;

public class RegistrationActivity extends AppCompatActivity {
    private Button btnBackToLogin, btnRegister;
    private EditText etFirstName, etLastName, etEmail, etPassword, etHoulyWage, etCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        textAndButtons();
        listeners();
    }

    public void textAndButtons(){
        btnBackToLogin = (Button)findViewById(R.id.btnBackToLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etLastName = (EditText)findViewById(R.id.etLastName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etHoulyWage = (EditText)findViewById(R.id.etHourlyWage);
        etCompany = (EditText)findViewById(R.id.etCompany);
    }

    public void listeners(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Firstname", etFirstName.getText().toString());
                map.put("Lastname", etLastName.getText().toString());
                map.put("Email", etEmail.getText().toString());
                map.put("Password", etPassword.getText().toString());
                map.put("HourlyWage", etHoulyWage.getText().toString());
                map.put("CompanyName", etCompany.getText().toString());
                new CreateUser(map, getApplicationContext());
            }
        });
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
