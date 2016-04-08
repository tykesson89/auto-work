package com.lhadalo.oladahl.autowork.activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();

        toolbar = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if(menuItem.isChecked()){
                    menuItem.setChecked(false);
                }
                else{
                    menuItem.setChecked(true);
                }

                switch(menuItem.getItemId()){
                    case R.id.drawer_jan:
                        Toast.makeText(getApplicationContext(), getResources().getStringArray(R.array.months)[0], Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_feb:
                        Toast.makeText(getApplicationContext(), getResources().getStringArray(R.array.months)[1], Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_mar:
                        Toast.makeText(getApplicationContext(), getResources().getStringArray(R.array.months)[2], Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_apr:
                        Toast.makeText(getApplicationContext(), getResources().getStringArray(R.array.months)[3], Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_may:
                        Toast.makeText(getApplicationContext(), getResources().getStringArray(R.array.months)[4], Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_jun:
                        Toast.makeText(getApplicationContext(), getResources().getStringArray(R.array.months)[5], Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_jul:
                        Toast.makeText(getApplicationContext(), getResources().getStringArray(R.array.months)[6], Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_aug:
                        Toast.makeText(getApplicationContext(), getResources().getStringArray(R.array.months)[7], Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_sep:
                        Toast.makeText(getApplicationContext(), getResources().getStringArray(R.array.months)[8], Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_oct:
                        Toast.makeText(getApplicationContext(), getResources().getStringArray(R.array.months)[9], Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_nov:
                        Toast.makeText(getApplicationContext(), getResources().getStringArray(R.array.months)[10], Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_dec:
                        Toast.makeText(getApplicationContext(), getResources().getStringArray(R.array.months)[11], Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.openDrawer, R.string.closeDrawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0);
            }
        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                //callback.onActionSettingsPressed();
                break;

            case R.id.action_log_out:
                //callback.onActionLogOutPressed();
                break;
            case R.id.action_add_workpass:
                //callback.onActionAddWorkpassPressed();
                break;
            case R.id.action_launch_test:
                //callback.onActionLaunchTestActivityPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonClickTry(){

        Intent a = new Intent(MainActivity.this, AddCompanyActivity.class);
        startActivity(a);

    }

    @Override
    public void onActionLaunchTestActivityPressed() {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);

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








