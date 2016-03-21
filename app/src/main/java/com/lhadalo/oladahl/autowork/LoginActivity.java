package com.lhadalo.oladahl.autowork;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btnLogin, btnCreateUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Creating database", " ");
        SQLiteDB sqLiteDB = new SQLiteDB(this);

        Log.d("Database created", " ");
//        if (sqLiteDB.isLoggedIn() == true) {
//            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
        sqLiteDB.getWritableDatabase();

        setContentView(R.layout.activity_login_2);
        textAndButtons();
        listeners();
    }

    public void textAndButtons(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnCreateUser = (Button)findViewById(R.id.btnCreateUser);

        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.login_title));

    }
    public void listeners(){
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016-03-11 Vad som händer nät man trycker på loginknappen. 
            }
        });
    }
}

