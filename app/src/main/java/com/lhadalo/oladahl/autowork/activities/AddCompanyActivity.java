package com.lhadalo.oladahl.autowork.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    SQLiteDB db = new SQLiteDB(AddCompanyActivity.this);

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

    public void onClickBtnAddCompany(String companyName, double hourly) {


        List<Company> list = new ArrayList<>();
        boolean exists = false;

        list = db.getAllCompanies();

        for(int i = 0; i < list.size(); i++) {
            String str = list.get(i).getCompanyName();
            Log.v(companyName, str);
            if(str.equals(companyName)) {
                exists = true;

            }
        }
        Log.v("ddd", "gdgd");
        if(exists == true) {

            CharSequence text = "Company already exists";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(AddCompanyActivity.this, text, duration);
            toast.show();
        }
        else {


            Company company = new Company(companyName, hourly);

            User user = db.getUser();
            int myID = user.getUserid();
            company.setUserId(myID);
            db.addCompany(company);
            fragment.spinner();
            CharSequence text = "Company added";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(AddCompanyActivity.this, text, duration);
            toast.show();


            fragment.setTextCompany(companyName);
            fragment.setTextHourly(String.valueOf(hourly));



        }
    }

    public void onClickBtnChangeCompany(String companyName, double hourly) {
        List<Company> list = new ArrayList<>();
        boolean exists = false;

        list = db.getAllCompanies();

        for(int i = 0; i < list.size(); i++) {
            String str = list.get(i).getCompanyName();

            if(companyName.equals(str)) {
                exists = true;
            }
        }
        if(exists == false) {

            CharSequence text = "Company doesn't exists";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(AddCompanyActivity.this, text, duration);
            toast.show();
        }
        else {
            Company company = new Company(companyName, hourly);
            db.changeCompany(company);
            fragment.setTextHourly(String.valueOf(hourly));

            CharSequence text = "Company updated";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(AddCompanyActivity.this, text, duration);
            toast.show();
        }
    }

    public void onClickBtnDeleteCompany(String companyName) {
        List<Company> list = new ArrayList<>();
        boolean exists = false;

        list = db.getAllCompanies();

        for(int i = 0; i < list.size(); i++) {
            String str = list.get(i).getCompanyName();


            if(companyName.equals(str)) {
                exists = true;
            }
        }
        if(exists == false) {

            CharSequence text = "Company not deleted";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(AddCompanyActivity.this, text, duration);
            toast.show();
        }
        else {

            db.deleteCompany(companyName);

            CharSequence text = "Company deleted";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(AddCompanyActivity.this, text, duration);
            toast.show();
            fragment.spinner();
            fragment.setTextHourly("");
            fragment.setTextCompany("");


        }

    }

}

