package com.lhadalo.oladahl.autowork.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.lhadalo.oladahl.autowork.DrawerListener;
import com.lhadalo.oladahl.autowork.database.DatabaseContract;
import com.lhadalo.oladahl.autowork.ListAdapter;
import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.database.FetchWorkpasses;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;
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
import UserPackage.Workpass;


public class MainActivity extends AppCompatActivity
        implements MainFragment.OnFragmentInteraction, ListAdapter.ItemClickListener {
    private MainFragment fragment;
    private List<Workpass> workpassList;
    private SQLiteDB database = new SQLiteDB(this);

    private ListAdapter adapter;
    ActionBarDrawerToggle drawerToggle;

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Får referens till header för att sätta namn och email på användare
        View headerLayout = navigationView.getHeaderView(0);
        User user = database.getUser();
        TextView headerName = (TextView) headerLayout.findViewById(R.id.header_name);
        TextView headerEmail = (TextView) headerLayout.findViewById(R.id.header_email);

        headerName.setText(String.format("%1$s %2$s", user.getFirstname(), user.getLastname()));
        headerEmail.setText(user.getEmail());

        //Får referens till inställningslayout
        LinearLayout settingsLayout = (LinearLayout) navigationView.findViewById(R.id.settings_row);

        //Sätter eventlistener till inställningslayout
        settingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionSettingsPressed();
            }
        });

        //Hämtar textview i inställningslayout och sätter rätt text.
        TextView txtSettings = (TextView) settingsLayout.getChildAt(0);
        txtSettings.setText("Settings");

        ImageView imgSettings = (ImageView) settingsLayout.getChildAt(1);
        imgSettings.setImageDrawable(getResources().getDrawable(R.drawable.ic_settings_black_18dp));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_main);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
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

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new DrawerListener(this));

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

        //Hämtar arbetspass baserat på vilken månad det är.
        FetchWorkpasses.newInstance(this, 1).execute(Calendar.getInstance().get(Calendar.MONTH));

    }

    public void closeDrawer() {
        drawerLayout.closeDrawers();
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
        switch (id) {
            case R.id.action_log_out:
                onActionLogOutPressed();
                break;
            case R.id.test:

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
        onActionAddWorkpassPressed();
        //createOptionsDialog("Choose action", new String[]{"Add Company", "Add Workpass"}, 1, -1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Tag.ADD_WORKPASS_REQUEST) {
                int month = data.getIntExtra(DatabaseContract.WorkpassEntry.MONTH, -1);
                if (month != -1) {
                    if (month == Calendar.getInstance().get(Calendar.MONTH)) {
                        Workpass lastAdded = database.getLastAddedWorkpass();
                        workpassList.add(lastAdded);
                        adapter.notifyDataSetChanged();
                        FetchWorkpasses.newInstance(this, Tag.ON_GET_STATISTICS).execute(0);
                    }
                }
            } else if (requestCode == Tag.UPDATE_WORKPASS_REQUEST) {
                int listPosition = data.getIntExtra(Tag.LIST_POSITION, -1);
                if (listPosition != -1) {
                    FetchWorkpasses.newInstance(this, Tag.ON_UPDATE_LIST).execute(Calendar.getInstance().get(Calendar.MONTH)); //TODO Känns lite dumt att hämta allt på nytt, får ändra sedan.
                }
            }
        }
    }


    public void getStatistics(List<Workpass> allWorkpasses) {

        double salary = 0, hours = 0;

        if (workpassList != null) {
            for (Workpass workpass : workpassList) {
                salary += workpass.getSalary();
                hours += workpass.getWorkingHours();
            }

            fragment.setTextTvSalary(String.valueOf(salary));
            fragment.setTextTvHours(String.valueOf(hours));
        }

        Workpass nextPass = null;
        boolean noNextPass = true;
        for (int i = 0; i < allWorkpasses.size() && noNextPass; i++) {
            if (allWorkpasses.get(i).getStartDateTime().get(Calendar.DAY_OF_MONTH)
                    >= Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {

                nextPass = allWorkpasses.get(i);
                noNextPass = false;
            }
        }

        if (nextPass != null) {
            fragment.setTextTvSalaryPass(String.valueOf(nextPass.getSalary()) + " Kr");
            fragment.setTextTvHoursPass(String.valueOf(nextPass.getWorkingHours()) + " h");
            fragment.setTextTvNextPass("Next pass: " + formatCalendarToString(nextPass.getStartDateTime()));
        } else {
            fragment.setTextTvSalaryPass("0.0 Kr");
            fragment.setTextTvHoursPass("0.0 h");
            fragment.setTextTvNextPass("Next pass:");
        }
    }

    private String formatCalendarToString(GregorianCalendar cal) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy MM dd HH:mm");

        String dateFormatted = fmt.format(cal.getTime());

        return dateFormatted;
    }

    @Override
    public void onItemClick(int position) {
        createOptionsDialog("Choose action", new String[]{"Delete item", "Change item"}, 2, position);
    }

    private void createOptionsDialog(String title, String[] options, final int source,
                                     final int listPosition) {
        AlertDialog.Builder optionDialog = new AlertDialog.Builder(this);

        optionDialog.setTitle(title);

        optionDialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                if (source == 1) {
                    switch (position) {
                        case 0:
                            onButtonClickTry();
                            break;
                        case 1:
                            onActionAddWorkpassPressed();
                            break;
                    }
                } else if (source == 2) {
                    switch (position) {
                        case 0:

                            Workpass modelToDelete = workpassList.get(listPosition);

                            if (database.deleteWorkpass(modelToDelete.getWorkpassID())) {
                                workpassList.remove(listPosition);
                                adapter.notifyDataSetChanged();
                                FetchWorkpasses.newInstance(MainActivity.this, Tag.ON_GET_STATISTICS).execute(0);

                                Toast.makeText(MainActivity.this, "Workpass Deleted",
                                        Toast.LENGTH_SHORT).show();
                            }

                            break;
                        case 1:
                            Workpass modelToChange = workpassList.get(listPosition);


                            Intent intent = new Intent(getApplicationContext(),
                                    AddWorkpassActivity.class);

                            intent.putExtra(DatabaseContract.WorkpassEntry.WORKPASS_ID, modelToChange.getWorkpassID());
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

    public void onCreateList(List<Workpass> workpasses) {
        workpassList = new ArrayList<>();
        for (Workpass workpass : workpasses) {
            workpassList.add(workpass);
        }

        adapter = new ListAdapter(this, workpassList);
        fragment.setListAdapter(adapter);
        FetchWorkpasses.newInstance(this, Tag.ON_GET_STATISTICS).execute(0);
    }

    public void updateList(List<Workpass> workpasses) {
        workpassList.clear();

        if (workpasses != null) {
            workpassList.addAll(workpasses);
        } else {
            Toast.makeText(MainActivity.this, "Inga pass hittades", Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();

        FetchWorkpasses.newInstance(this, Tag.ON_GET_STATISTICS).execute(0);

    }
}








