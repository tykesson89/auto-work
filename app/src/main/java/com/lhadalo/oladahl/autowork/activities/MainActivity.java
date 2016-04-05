package com.lhadalo.oladahl.autowork.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.SQLiteDB;
import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.fragments.MainFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import UserPackage.Company;
import UserPackage.WorkpassModel;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteraction {
    private MainFragment fragment;
    private ArrayList<Long> ids = new ArrayList<>();
    private int companyId;
    private SQLiteDB database;
    private ArrayList<WorkpassModel> workpassModels;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();


    }


    private void initFragment() {
        fragment = new MainFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);

         getMonthSalary(month);

    }

    @Override
    public void onActionSettingsPressed() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActionLogOutPressed() {
        SQLiteDB sqLiteDB = new SQLiteDB(MainActivity.this);
        sqLiteDB.deleteAll();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onActionAddWorkpassPressed() {
        Intent intent = new Intent(this, AddWorkpassActivity.class);
        startActivityForResult(intent, Tag.ADD_WORKPASS_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == Tag.ADD_WORKPASS_REQUEST){
                companyId = data.getIntExtra(Tag.WORKPASS_ID, -1);
            }
        }
    }

    public void onClickGetStart(View view) {
        List<WorkpassModel> models = database.getAllWorkpasses();

        for(WorkpassModel m : models){
            Log.v(Tag.LOGTAG, m.toString());
        }
    }
    public void getMonthSalary(int month) {
        database = new SQLiteDB(MainActivity.this);

        ArrayList<WorkpassModel> workpassModels;
        workpassModels= database.getSalaryAndDate();


        double salary = 0;

        ArrayList<WorkpassModel> list = new ArrayList<WorkpassModel>();

        for (int i = 0; i < workpassModels.size(); i++) {
            if (workpassModels.get(i).getEndDateTime().getMonth() == month) {
                list.add(workpassModels.get(i));
            }

        }
        for (int i = 0; i < list.size(); i++) {
            salary += list.get(i).getSalary();

        }
        String sal = String.valueOf(salary);
        fragment.setTextTvSalary(sal);
    }

}
