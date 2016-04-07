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

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import UserPackage.Company;
import UserPackage.WorkpassModel;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteraction {
    private MainFragment fragment;
    private ArrayList<Long> ids = new ArrayList<>();
    private int companyId;
    private String nameCompany;
    private SQLiteDB database = new SQLiteDB(this);


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
        getHours(month);
        getNextPassHour(month);
        getNextPassSalary(month);
        //getDate(month);
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

    public void onButtonClickTry(){

        Intent a = new Intent(MainActivity.this, AddCompanyActivity.class);
        startActivity(a);

    }

    @Override
    public void onActionAddWorkpassPressed() {
        Intent intent = new Intent(this, AddWorkpassActivity.class);
        startActivityForResult(intent, Tag.ADD_WORKPASS_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<WorkpassModel> workpassModels;

        if (resultCode == RESULT_OK) {
            if (requestCode == Tag.ADD_WORKPASS_REQUEST) {
                workpassModels = database.getAllWorkpasses();

                for(WorkpassModel m : workpassModels){
                    Log.v(Tag.LOGTAG, m.toString());
                }
            }
        }
    }

    public void getMonthSalary(int month) {
        database = new SQLiteDB(MainActivity.this);

        ArrayList<WorkpassModel> workpassModels;
        workpassModels = database.getSalaryAndDate();


        double salary = 0;

        ArrayList<WorkpassModel> list = new ArrayList<WorkpassModel>();

        for (int i = 0; i < workpassModels.size(); i++) {
            int modelMonth = workpassModels.get(i).getEndDateTime().get(Calendar.MONTH);
            if (modelMonth == month) {
                list.add(workpassModels.get(i));
            }

        }
        for (int i = 0; i < list.size(); i++) {
            salary += list.get(i).getSalary();

        }
        String sal = String.valueOf(salary);
        fragment.setTextTvSalary(sal + " Kr ");
    }

    public void getHours(int month) {
        database = new SQLiteDB(MainActivity.this);

        ArrayList<WorkpassModel> workpassModels;
        workpassModels = database.getHours();


        double hours = 0;

        ArrayList<WorkpassModel> list = new ArrayList<WorkpassModel>();

        for (int i = 0; i < workpassModels.size(); i++) {
            int modelMonth = workpassModels.get(i).getEndDateTime().get(Calendar.MONTH);
            if (modelMonth == month) {
                list.add(workpassModels.get(i));
            }

        }
        for (int i = 0; i < list.size(); i++) {
            hours += list.get(i).getWorkingHours();

        }
        String sal = String.valueOf(hours);
        fragment.setTextTvHours(sal + " h ");
    }

    public void getNextPassHour(int month) {
        database = new SQLiteDB(MainActivity.this);

        ArrayList<WorkpassModel> workpassModels;
        workpassModels = database.getNextPassHour();


        double hours = 0;

        ArrayList<WorkpassModel> list = new ArrayList<WorkpassModel>();

        for (int i = 0; i < workpassModels.size(); i++) {

            int modelMonth = workpassModels.get(i).getEndDateTime().get(Calendar.MONTH);
            if (modelMonth == month) {
                list.add(workpassModels.get(i));
            }
        }
        for (int i = 0; i < list.size(); i++) {
            hours = list.get(i).getWorkingHours();

        }
        String sal = String.valueOf(hours);
        fragment.setTextTvHoursPass(sal + " h ");
    }

    public void getNextPassSalary(int month) {
        database = new SQLiteDB(MainActivity.this);

        ArrayList<WorkpassModel> workpassModels;
        workpassModels = database.getNextPassSalary();


        double salary = 0;

        ArrayList<WorkpassModel> list = new ArrayList<WorkpassModel>();

        for (int i = 0; i < workpassModels.size(); i++) {

            int modelMonth = workpassModels.get(i).getEndDateTime().get(Calendar.MONTH);
            if (modelMonth == month) {
                list.add(workpassModels.get(i));
            }
        }
        for (int i = 0; i < list.size(); i++) {
            salary = list.get(i).getSalary();

        }
        String sal = String.valueOf(salary);
        fragment.setTextTvSalaryPass(sal + " Kr ");
    }

    public void getDate(int month) {
        database = new SQLiteDB(MainActivity.this);

        ArrayList<WorkpassModel> workpassModels;
        workpassModels = database.showDate();
        GregorianCalendar startTime = null ;

        ArrayList<WorkpassModel> list = new ArrayList<WorkpassModel>();

        for (int i = 0; i < workpassModels.size(); i++) {

            int modelMonth = workpassModels.get(i).getEndDateTime().get(Calendar.MONTH);
            if (modelMonth == month) {
                list.add(workpassModels.get(i));
            }

        }
        for (int i = 0; i < list.size(); i++) {
            startTime = list.get(i).getStartDateTime();
        }

        String strStartTime = formatCalendarToString(startTime);
        fragment.setTextTvNextPass(strStartTime);
    }

    private String formatCalendarToString(GregorianCalendar cal) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy MM dd HH:mm");

        String dateFormatted = fmt.format(cal.getTime());

        return dateFormatted;
    }

    private GregorianCalendar formatStringToCalendar(String str) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy MM dd HH:mm");

        Date date = null;
        try {
            date = fmt.parse(str);

        } catch (ParseException ex){
            ex.printStackTrace();
        }

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        return cal;
    }

}








