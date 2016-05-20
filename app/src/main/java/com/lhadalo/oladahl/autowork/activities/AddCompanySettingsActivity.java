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
import com.lhadalo.oladahl.autowork.StartService;

import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.database.DatabaseContract;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;
import com.lhadalo.oladahl.autowork.fragments.AddCompanyFragment;

import java.util.ArrayList;
import java.util.List;

import UserPackage.Company;
import UserPackage.User;

public class AddCompanySettingsActivity extends AppCompatActivity {
    private AddCompanyFragment fragment;
    SQLiteDB db = new SQLiteDB(AddCompanySettingsActivity.this);
    Company companyToChange;
    private long companyId;
    private EditText txtAddCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company_settings);
        Intent data = getIntent();

        txtAddCompany = (EditText)findViewById(R.id.add_comp);
        final EditText txtAddHourly = (EditText)findViewById(R.id.add_hourly);

        Button buttonAdd = (Button)findViewById(R.id.btn_add);

        final int request = data.getIntExtra(Tag.REQUEST_CODE, -1);

        if(request == Tag.ADD_COMPANY_REQUEST){
            assert buttonAdd != null;
            buttonAdd.setText(getString(R.string.add));
        }
        else{
            companyId = data.getLongExtra(DatabaseContract.CompanyEntry.COMPANY_ID, -1);
            companyToChange = db.getCompany(companyId);
            txtAddCompany.setText(companyToChange.getCompanyName());
            txtAddHourly.setText(String.valueOf(companyToChange.getHourlyWage()));

            txtAddCompany.setEnabled(false);
            buttonAdd.setText("Change");

        }


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

            CharSequence text = getString(R.string.companyExists);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(AddCompanySettingsActivity.this, text, duration);
            toast.show();
        }
        else {


            Company company = new Company(companyName, hourly);

            User user = db.getUser();
            int myID = user.getUserid();
            company.setUserId(myID);
            company.setIsSynced(Tag.IS_NOT_SYNCED);
            company.setActionTag(Tag.ON_CREATE_COMPANY);

            db.addCompany(company);

            CharSequence text = getString(R.string.companyAdded);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(AddCompanySettingsActivity.this, text, duration);
            toast.show();


            if(companyName!=null){
                StartService.startService(this);
                setResult(RESULT_OK);
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
            Toast toast = Toast.makeText(AddCompanySettingsActivity.this, text, duration);
            toast.show();
        }
        else {
            companyToChange.setCompanyName(companyName);
            companyToChange.setHourlyWage(hourly);
            companyToChange.setIsSynced(Tag.IS_NOT_SYNCED);
            companyToChange.setActionTag(Tag.ON_CHANGE_COMPANY);

            db.changeCompany(companyToChange);

            CharSequence text = getString(R.string.companyUpdated);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(AddCompanySettingsActivity.this, text, duration);
            toast.show();

            Intent data = new Intent();
            data.putExtra(Tag.LIST_POSITION, getIntent().getIntExtra(Tag.LIST_POSITION, -1));
            data.putExtra(DatabaseContract.CompanyEntry.COMPANY_ID, companyId);

            StartService.startService(this);
            setResult(RESULT_OK, data);
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

            CharSequence text = getString(R.string.companyNotDeleted);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(AddCompanySettingsActivity.this, text, duration);
            toast.show();
        }
        else {

            db.deleteCompany(companyName);

            CharSequence text = getString(R.string.companyDeleted);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(AddCompanySettingsActivity.this, text, duration);
            toast.show();

        }

    }

}

