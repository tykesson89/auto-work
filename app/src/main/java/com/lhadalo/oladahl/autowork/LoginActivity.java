package com.lhadalo.oladahl.autowork;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity
        implements LoginFragment.OnFragmentInteraction {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.login_container, new LoginFragment());
        fragmentTransaction.commit();
    }


    @Override
    public void onClickLogin(String email, String password) {
        Toast.makeText(this, email + "\n" + password, Toast.LENGTH_SHORT).show();
    }
}
