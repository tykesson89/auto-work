package com.lhadalo.oladahl.autowork;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by oladahl on 16-05-15.
 */
public class AccountActivity extends AppCompatActivity{
    AccountFragment fragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account);

        initFragment();
    }

    private void initFragment(){
        fragment = new AccountFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_account, fragment).commit();
    }
}
