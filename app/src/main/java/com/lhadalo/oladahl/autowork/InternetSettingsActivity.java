package com.lhadalo.oladahl.autowork;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by oladahl on 16-04-03.
 */
public class InternetSettingsActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new InternetSettingsFragment())
                .commit();


    }
}
