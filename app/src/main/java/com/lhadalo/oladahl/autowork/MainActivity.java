package com.lhadalo.oladahl.autowork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent mainIntent = new Intent(MainActivity.this, SplashScreenActivity.class);
        MainActivity.this.startActivity(mainIntent);
        MainActivity.this.finish();
    }
}
