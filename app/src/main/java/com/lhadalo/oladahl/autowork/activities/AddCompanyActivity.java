package com.lhadalo.oladahl.autowork.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.SQLiteDB;
import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.fragments.AddCompanyFragment;
import com.lhadalo.oladahl.autowork.fragments.SettingsFragment;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import UserPackage.Company;
import UserPackage.User;

public class AddCompanyActivity extends AppCompatActivity implements AddCompanyFragment.OnFragmentInteraction {
    private AddCompanyFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);
        initFragment();

    }

    private void initFragment() {
        fragment = new AddCompanyFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_add_company, fragment).commit();
    }
    protected void onStart() {
        super.onStart();

    }
public void onClickBtnAddCompany(String companyName, double hourly){

    Company company = new Company(companyName, hourly);

    SQLiteDB db = new SQLiteDB(AddCompanyActivity.this);
    int myID = db.getUserId(AddCompanyActivity.this);
    company.setUserId(myID);
    db.addCompanyLocal(company);
    fragment.spinner();
    CharSequence text = "Company added";
    int duration = Toast.LENGTH_SHORT;
    Toast toast = Toast.makeText(AddCompanyActivity.this, text, duration);
    toast.show();



}
    public void onClickBtnChangeCompany(String companyName, double hourly){


    }

    public void onClickBtnDeleteCompany(String companyName, double hourly){


    }

}

