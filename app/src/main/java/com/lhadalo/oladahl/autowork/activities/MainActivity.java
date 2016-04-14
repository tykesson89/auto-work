package com.lhadalo.oladahl.autowork.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lhadalo.oladahl.autowork.ListAdapter;
import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.SQLiteDB;
import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.fragments.MainFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import UserPackage.User;
import UserPackage.WorkpassModel;
import com.lhadalo.oladahl.autowork.WorkpassContract.WorkpassEntry;

public class MainActivity extends AppCompatActivity
        implements MainFragment.OnFragmentInteraction, ListAdapter.ItemClickListener {
    private MainFragment fragment;
    private ArrayList<Long> ids = new ArrayList<>();
    private int companyId;
    private String nameCompany;
    private SQLiteDB database = new SQLiteDB(this);
    private List<WorkpassModel> workpassList;
    private ListAdapter adapter;

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

        //Får referens till header för att sätta namn och email på användare
        View headerLayout = navigationView.getHeaderView(0);
        User user = database.getUser();
        TextView headerName = (TextView)headerLayout.findViewById(R.id.header_name);
        TextView headerEmail = (TextView)headerLayout.findViewById(R.id.header_email);

        headerName.setText(String.format("%1$s %2$s", user.getFirstname(), user.getLastname()));
        headerEmail.setText(user.getEmail());

        //Får referens till inställningslayout
        LinearLayout settingsLayout = (LinearLayout)navigationView.findViewById(R.id.settings_row);

        //Sätter eventlistener till inställningslayout
        settingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionSettingsPressed();
            }
        });

        //Hämtar textview i inställningslayout och sätter rätt text.
        TextView txtSettings = (TextView)settingsLayout.getChildAt(0);
        txtSettings.setText("Settings");

        ImageView imgSettings = (ImageView)settingsLayout.getChildAt(1);
        imgSettings.setImageDrawable(getResources().getDrawable(R.drawable.ic_settings_black_18dp));

        //Får referens till logga ut-layout
        LinearLayout logOutLayout = (LinearLayout)navigationView.findViewById(R.id.log_out_row);

        //Sätter eventlistener till lagga ut-layout
        logOutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //Hämtar textview i logga ut-layout och sätter rätt text
        TextView txtLogOut = (TextView)logOutLayout.getChildAt(0);
        txtLogOut.setText("Log out");

        ImageView imgLogOut = (ImageView)logOutLayout.getChildAt(1);
        imgLogOut.setImageDrawable(getResources().getDrawable(R.drawable.ic_exit_to_app_black_18dp));


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if(menuItem.isChecked()) {
                    menuItem.setChecked(false);
                }
                else {
                    menuItem.setChecked(true);
                }

                switch(menuItem.getItemId()) {
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
                R.string.openDrawer, R.string.closeDrawer) {
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

        workpassList = database.getAllWorkpasses();

        if(workpassList != null) {
            adapter = new ListAdapter(this, workpassList);
        }
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
        SQLiteDB sqLiteDB = new SQLiteDB(this);
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);


        getMonthSalary(month);
        getHours(month);
        getNextPassHour(month);
        getNextPassSalary(month);

        if(sqLiteDB.haveWorkpass()== true) {
            getDate(month, day);
        }

        if(workpassList != null) {
            fragment.setListAdapter(adapter);
        }
    }


    public void onActionSettingsPressed() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


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
        switch(id) {
            case R.id.action_log_out:
                onActionLogOutPressed();
                break;
            case R.id.test:
                List<WorkpassModel> testList = database.getAllWorkpasses();
                for(WorkpassModel m : testList){
                    Log.v(Tag.LOGTAG, m.toString());
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonClickTry() {

        Intent a = new Intent(MainActivity.this, AddCompanyActivity.class);
        startActivity(a);

    }

    public void onActionLaunchTestActivityPressed() {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);

    }


    public void onActionAddWorkpassPressed() {
        Intent intent = new Intent(this, AddWorkpassActivity.class);
        intent.putExtra(Tag.REQUEST_CODE, Tag.ADD_WORKPASS_REQUEST);
        startActivityForResult(intent, Tag.ADD_WORKPASS_REQUEST);
    }

    @Override
    public void onFABPressed() {
        createOptionsDialog("Choose action", new String[]{"Add Company", "Add Workpass"}, 1, -1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == Tag.ADD_WORKPASS_REQUEST) {
                WorkpassModel lastAdded = database.getLastAddedWorkpass();
                workpassList.add(lastAdded);

                adapter.notifyDataSetChanged();
            }
            else if(requestCode == Tag.UPDATE_WORKPASS_REQUEST){
                int listPosition = data.getIntExtra(Tag.LIST_POSITION, -1);
                if(listPosition != -1){
                    long workpassId = workpassList.get(listPosition).getId();
                    WorkpassModel changedModel = database.getWorkpass(workpassId);
                    workpassList.set(listPosition, changedModel);
                    adapter.notifyDataSetChanged();
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

        for(int i = 0; i < workpassModels.size(); i++) {
            int modelMonth = workpassModels.get(i).getEndDateTime().get(Calendar.MONTH);
            if(modelMonth == month) {
                list.add(workpassModels.get(i));
            }
        }
        for(int i = 0; i < list.size(); i++) {
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

        for(int i = 0; i < workpassModels.size(); i++) {
            int modelMonth = workpassModels.get(i).getEndDateTime().get(Calendar.MONTH);
            if(modelMonth == month) {
                list.add(workpassModels.get(i));
            }

        }
        for(int i = 0; i < list.size(); i++) {
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

        for(int i = 0; i < workpassModels.size(); i++) {

            int modelMonth = workpassModels.get(i).getEndDateTime().get(Calendar.MONTH);
            if(modelMonth == month) {
                list.add(workpassModels.get(i));
            }
        }
        for(int i = 0; i < list.size(); i++) {
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

        for(int i = 0; i < workpassModels.size(); i++) {

            int modelMonth = workpassModels.get(i).getEndDateTime().get(Calendar.MONTH);
            if(modelMonth == month) {
                list.add(workpassModels.get(i));
            }
        }
        for(int i = 0; i < list.size(); i++) {
            salary = list.get(i).getSalary();

        }
        String sal = String.valueOf(salary);
        fragment.setTextTvSalaryPass(sal + " Kr ");
    }

    public void getDate(int month, int day) {
        database = new SQLiteDB(MainActivity.this);

        ArrayList<WorkpassModel> workpassModels;
        workpassModels = database.showDate();
        GregorianCalendar startTime = null;

        ArrayList<WorkpassModel> list = new ArrayList<WorkpassModel>();

        for(int i = 0; i < workpassModels.size(); i++) {

            int modelMonth = workpassModels.get(i).getStartDateTime().get(Calendar.MONTH);
            int modelDay = workpassModels.get(i).getStartDateTime().get(Calendar.DAY_OF_MONTH);
            int modelHour = workpassModels.get(i).getStartDateTime().get(Calendar.HOUR);
            if(modelMonth == month && modelDay == day ) {
                list.add(workpassModels.get(i));
            }

        }
        for(int i = 0; i < list.size(); i++) {
            startTime = list.get(i).getStartDateTime();
        }

        String strStartTime = formatCalendarToString(startTime);
        fragment.setTextTvNextPass(" Next pass: " + strStartTime);
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

        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        return cal;
    }

    @Override
    public void onItemClick(int position) {
        createOptionsDialog("Choose action", new String[]{"Delete item", "Change item"}, 2, position);

        //Toast.makeText(this, "Clicked position: " + String.valueOf(position), Toast.LENGTH_SHORT).show();
    }

    private void createOptionsDialog(String title, String[] options, final int source,
                                     final int listPosition) {
        AlertDialog.Builder optionDialog = new AlertDialog.Builder(this);

        optionDialog.setTitle(title);

        optionDialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                if(source == 1) {
                    switch(position) {
                        case 0:
                            onButtonClickTry();
                            break;
                        case 1:
                            onActionAddWorkpassPressed();
                            break;
                    }
                }
                else if(source == 2) {
                    switch(position) {
                        case 0:

                            WorkpassModel modelToDelete = workpassList.get(listPosition);

                            if(database.deleteWorkpass(modelToDelete.getId())) {
                                workpassList.remove(listPosition);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Workpass Deleted",
                                        Toast.LENGTH_SHORT).show();
                            }

                            break;
                        case 1:
                            WorkpassModel modelToChange = workpassList.get(listPosition);


                            Intent intent = new Intent(getApplicationContext(),
                                    AddWorkpassActivity.class);

                            intent.putExtra(WorkpassEntry.WORKPASS_ID, modelToChange.getId());
                            intent.putExtra(Tag.REQUEST_CODE, Tag.UPDATE_WORKPASS_REQUEST);
                            intent.putExtra(Tag.LIST_POSITION, listPosition);

                            startActivityForResult(intent, Tag.UPDATE_WORKPASS_REQUEST);
                            break;
                    }
                }
            }
        });

        optionDialog.show();
    }
}








