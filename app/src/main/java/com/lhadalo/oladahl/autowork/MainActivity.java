package com.lhadalo.oladahl.autowork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteDB sqLiteDB = new SQLiteDB(getApplicationContext());
        setContentView(R.layout.activity_main);
        if(sqLiteDB.isLoggedIn() == false) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            // TODO: 2016-03-11 Continue MainActivity. 
        }
        
    }
}
