package com.lhadalo.oladahl.autowork;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import UserPackage.User;

public class MainActivity extends AppCompatActivity {
        private Button btnSettings, btnLogOut;
        private TextView tvName;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        componentsInits();
        listeners();
        SQLiteDB sqLiteDB = new SQLiteDB(MainActivity.this);
        String name = sqLiteDB.getFirstName();
        tvName.setText(name);
    }


    public void componentsInits(){
        btnSettings = (Button)findViewById(R.id.btnSettings);
        btnLogOut = (Button)findViewById(R.id.btnLogOut);
        tvName = (TextView)findViewById(R.id.tvName);
    }

    public void listeners(){
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SQLiteDB sqLiteDB = new SQLiteDB(MainActivity.this);
                sqLiteDB.deleteAll();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
