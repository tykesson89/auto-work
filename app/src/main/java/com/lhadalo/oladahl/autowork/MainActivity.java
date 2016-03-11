package com.lhadalo.oladahl.autowork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent mainIntent = new Intent(MainActivity.this, SplashScreenActivity.class);
        MainActivity.this.startActivity(mainIntent);
        MainActivity.this.finish();
        SQLiteDB sqLiteDB = new SQLiteDB(getApplicationContext());
        setContentView(R.layout.activity_main);
        if(sqLiteDB.isLoggedIn() != true) {
            // TODO: 2016-03-11 Check if user is logged in. 
        }else{
            // TODO: 2016-03-11 Continue MainActivity. 
        }
        
    }
}
