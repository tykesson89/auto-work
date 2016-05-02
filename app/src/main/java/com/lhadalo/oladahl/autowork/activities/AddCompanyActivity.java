package com.lhadalo.oladahl.autowork.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;
import com.lhadalo.oladahl.autowork.fragments.AddCompanyFragment;

import java.util.ArrayList;
import java.util.List;

import UserPackage.Company;
import UserPackage.User;

public class AddCompanyActivity extends AppCompatActivity {
    private AddCompanyFragment fragment;
    SQLiteDB db = new SQLiteDB(AddCompanyActivity.this);
    Company companyToChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company_settings);


        final EditText txtAddCompany = (EditText)findViewById(R.id.add_comp);
        final EditText txtAddHourly = (EditText)findViewById(R.id.add_hourly);

        Button buttonAdd = (Button)findViewById(R.id.btn_add);

        final int request = getIntent().getIntExtra(Tag.REQUEST_CODE, -1);
        int companyId = getIntent().getIntExtra("Company_id", -1);

        if(request == Tag.ADD_COMPANY_REQUEST){
            assert buttonAdd != null;
            buttonAdd.setText("Add");
        }
        else{
            companyToChange = db.getCompany(companyId);
            txtAddCompany.setText(companyToChange.getCompanyName());
            txtAddHourly.setText(String.valueOf(companyToChange.getHourlyWage()));

            buttonAdd.setText("Change");
        }

        assert buttonAdd != null;
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(request == Tag.ADD_COMPANY_REQUEST) {
                    onClickBtnAddCompany(txtAddCompany.getText().toString(),
                            Double.parseDouble(txtAddHourly.getText().toString()));
                }
                else{
                    onClickBtnChangeCompany(txtAddCompany.getText().toString(),
                            Double.parseDouble(txtAddHourly.getText().toString()));
                }
            }
        });
    }

    private void initFragment() {
        fragment = new AddCompanyFragment();
        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_add_company, fragment).commit();*/
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

            finish();
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

            fragment.setTextHourly("");
            fragment.setTextCompany("");


        }

    }

}

