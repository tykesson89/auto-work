package com.lhadalo.oladahl.autowork;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by oladahl on 16-03-07.
 */
public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        getSupportFragmentManager().beginTransaction().replace(R.id.create_account_container,
                new CreateAccountFragment()).commit();
    }
}
