package com.lhadalo.oladahl.autowork.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;
import com.lhadalo.oladahl.autowork.fragments.AddCompanyFragment;

import java.util.ArrayList;
import java.util.List;

import UserPackage.Company;
import UserPackage.User;

public class AddCompanyActivity extends AppCompatActivity{

    SQLiteDB db = new SQLiteDB(AddCompanyActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company_settings);

        final EditText etCompany, etWage;
        etCompany = (EditText)findViewById(R.id.add_comp);
        etWage = (EditText)findViewById(R.id.add_hourly);

        Button btnAdd = (Button)findViewById(R.id.btn_add);



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBtnAddCompany(etCompany.getText().toString(), Double.parseDouble(etWage.getText().toString()));
            }
        });
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

            CharSequence text = "Company added";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(AddCompanyActivity.this, text, duration);
            toast.show();


            if(companyName!=null){
                finish();
            }

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
        }

    }

}

