package com.lhadalo.oladahl.autowork.activities;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lhadalo.oladahl.autowork.DrawerListener;
import com.lhadalo.oladahl.autowork.InternetService;
import com.lhadalo.oladahl.autowork.SpinnerListener;
import com.lhadalo.oladahl.autowork.StartService;
import com.lhadalo.oladahl.autowork.TestActivity;
import com.lhadalo.oladahl.autowork.database.DatabaseContract;
import com.lhadalo.oladahl.autowork.ListAdapter;
import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.database.FetchWorkpasses;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;
import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.fragments.MainFragmentNew;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import UserPackage.Company;
import UserPackage.User;
import UserPackage.Workpass;



public class MainActivity extends AppCompatActivity
        implements MainFragmentNew.OnFragmentInteraction, ListAdapter.ItemClickListener {
    private MainFragmentNew fragment;
    private List<Workpass> workpasses;
    private SQLiteDB database = new SQLiteDB(this);

    private ListAdapter adapter;
    ActionBarDrawerToggle drawerToggle;

    private int drawerItemPressed;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private CoordinatorLayout coordinatorLayout;
    private double salary;
    private double hours;
    private Spinner spinner;
    private Menu menu;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();

        toolbar = (Toolbar)findViewById(R.id.toolbar_main);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Hämtar arbetspass baserat på vilken månad det är.
        FetchWorkpasses.newInstance(this, Tag.ON_CREATE_LIST).execute(Calendar.getInstance().get(Calendar.MONTH));

        //Spinner
        spinner = (Spinner)toolbar.getChildAt(0);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.months,  R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        setDropdownWidth();
        spinner.setAdapter(spinnerAdapter);

        spinner.setSelection(Calendar.getInstance().get(Calendar.MONTH), true);
        spinner.setOnItemSelectedListener(SpinnerListener.newInstance(this));

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

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_main);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                switch (drawerItemPressed){
                    case R.id.item_profile:
                        drawerItemPressed = -1;
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        break;
                    case R.id.item_companies:
                        drawerItemPressed = -1;
                        startActivity(new Intent(getApplicationContext(), AddCompanyActivity.class));
                        break;
                }
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
        fragment = new MainFragmentNew();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setDropdownWidth(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();

        display.getSize(size);
        int width = size.x;

        width -= 300;
        spinner.setDropDownWidth(width);

        //spinner.setDropDownHorizontalOffset(100);

    }



    @Override
    protected void onStart() {
        super.onStart();

        invalidateOptionsMenu();
    }


    @Override
    public void setCoordinatorLayout(CoordinatorLayout layout) {
        this.coordinatorLayout = layout;
    }

    public void showSnackbar(){
        Snackbar.make(coordinatorLayout, "Workpass Deleted", Snackbar.LENGTH_SHORT).show();
    }

    public void closeDrawer(int drawerItemPressed) {
        this.drawerItemPressed = drawerItemPressed;
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

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.lhadalo.oladahl.autowork.InternetService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.databasetest).setTitle("Hej");
        return super.onPrepareOptionsMenu(menu);
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
                startActivity(new Intent(this, TestActivity.class));
                break;
            case R.id.showSalary1:
                Intent intent = new Intent(MainActivity.this, SalaryWithTax.class);
                intent.putExtra("salary",salary );
                startActivity(intent);
                break;
            case R.id.databasetest:
                List<Workpass> workpasses = database.getAllWorkpasses();
                for (Workpass w : workpasses) {
                    Log.v(Tag.LOGTAG, w.toString());
                }
                List<Company> companies = database.getAllCompanies();
                for (Company c : companies) {
                    Log.v(Tag.LOGTAG, c.toString());
                }
                break;
            case R.id.isservicerunning:
                Toast.makeText(this, String.valueOf(isMyServiceRunning()),
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.hasconnection:
                Toast.makeText(this, String.valueOf(isConnected(this)), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onButtonClickTry() {
        Intent a = new Intent(MainActivity.this, AddCompanySettingsActivity.class);
        startActivity(a);
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

    public void onCreateList(List<Workpass> workpasses) {
        this.workpasses = workpasses;

        adapter = new ListAdapter(MainActivity.this, this, workpasses);
        fragment.setListAdapter(adapter);
        FetchWorkpasses.newInstance(this, Tag.ON_GET_STATISTICS).execute(0);
    }

    public void updateList(List<Workpass> workpasses) {
        this.workpasses.clear();

        if (workpasses != null) {
            this.workpasses.addAll(workpasses);
        }
        else {
            Toast.makeText(MainActivity.this, "Inga pass hittades", Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();

        FetchWorkpasses.newInstance(this, Tag.ON_GET_STATISTICS).execute(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            StartService.startService(getBaseContext());
            if (requestCode == Tag.ADD_WORKPASS_REQUEST) {
                int month = data.getIntExtra(DatabaseContract.WorkpassEntry.MONTH, -1);
                if (month != -1) {
                    if (month == Calendar.getInstance().get(Calendar.MONTH)) {
                        Workpass lastAdded = database.getLastAddedWorkpass();
                        workpasses.add(lastAdded);
                        adapter.notifyDataSetChanged();
                        FetchWorkpasses.newInstance(this, Tag.ON_GET_STATISTICS).execute(0);
                    }
                }
            }
            else if (requestCode == Tag.UPDATE_WORKPASS_REQUEST) {
                //int listPosition = data.getIntExtra(Tag.LIST_POSITION, -1);
                //if (listPosition != -1) {
                //}
                FetchWorkpasses.newInstance(this, Tag.ON_UPDATE_LIST).execute(Calendar.getInstance().get(Calendar.MONTH)); //TODO Känns lite dumt att hämta allt på nytt, får ändra sedan.
                FetchWorkpasses.newInstance(this, Tag.ON_GET_STATISTICS).execute(0);
            }
        }
        else if (resultCode == Tag.RESULT_WORKPASS_DELETED) {
            int positionToDelete = data.getIntExtra(Tag.LIST_POSITION, -1);
            workpasses.remove(positionToDelete);
            adapter.notifyDataSetChanged();
            StartService.startService(this);
            FetchWorkpasses.newInstance(this, Tag.ON_GET_STATISTICS).execute(0);
            //Waiter.newInstance(this).execute();
            Toast.makeText(MainActivity.this, "Arbetspass borttaget", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    public void getStatistics(List<Workpass> allWorkpasses) {
        Calendar now = Calendar.getInstance();
        salary = 0;
        hours = 0;

        if (workpasses != null) {
            for (Workpass workpass : workpasses) {
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
                    >= now.get(Calendar.DAY_OF_MONTH)) {

                nextPass = allWorkpasses.get(i);
                noNextPass = false;
            }
        }

        if (nextPass != null) {
            fragment.setTextTvSalaryPass(String.valueOf(nextPass.getSalary()) + " Kr");
            fragment.setTextTvHoursPass(String.valueOf(nextPass.getWorkingHours()) + " h");
            fragment.setTextTvNextPass("Next pass: " + formatCalendarToString(nextPass.getStartDateTime()));
        }
        else {
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
        Intent intent = new Intent(this, WorkpassViewerActivity.class);
        intent.putExtra(DatabaseContract.WorkpassEntry.WORKPASS_ID, workpasses.get(position).getWorkpassID());
        intent.putExtra(Tag.LIST_POSITION, position);

        startActivityForResult(intent, Tag.UPDATE_WORKPASS_REQUEST);
        //createOptionsDialog("Choose action", new String[]{"Delete item", "Change item"}, 2, position);
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
                }
                else if (source == 2) {
                    switch (position) {
                        case 0:
                            Intent serviceIntent = new Intent(getBaseContext(), InternetService.class);
                            startService(serviceIntent);

                            Workpass modelToDelete =
                                    database.getWorkpass(workpasses.get(listPosition).getWorkpassID());

                            //Ifall arbetspass inte är synkat ska ingen stnk utföras
                            if (modelToDelete.getIsSynced() == Tag.IS_NOT_SYNCED) {
                                database.deleteWorkpass(modelToDelete);
                            }
                            else {
                                modelToDelete.setActionTag(Tag.ON_DELETE_WORKPASS);
                                modelToDelete.setIsSynced(Tag.IS_NOT_SYNCED);
                                database.updateWorkpass(modelToDelete);
                            }

                            workpasses.remove(listPosition);
                            adapter.notifyDataSetChanged();
                            FetchWorkpasses.newInstance(MainActivity.this, Tag.ON_GET_STATISTICS).execute(0);

                            Toast.makeText(MainActivity.this, "Workpass Deleted",
                                    Toast.LENGTH_SHORT).show();


                            break;
                        case 1:
                            Workpass modelToChange = workpasses.get(listPosition);


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

    private static class Waiter extends AsyncTask<Void, Void, Void> {
        MainActivity activity;

        public static Waiter newInstance(Context context){
            return new Waiter(context);
        }

        public Waiter(Context context){
            activity = (MainActivity)context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            activity.showSnackbar();
        }
    }
}








