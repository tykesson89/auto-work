package com.lhadalo.oladahl.autowork.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.lhadalo.oladahl.autowork.database.AddWorkpassDB;
import com.lhadalo.oladahl.autowork.database.DatabaseContract.WorkpassEntry;
import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;
import com.lhadalo.oladahl.autowork.Tag;

import UserPackage.User;
import UserPackage.Workpass;

import com.lhadalo.oladahl.autowork.fragments.AddWorkpassFragment;
import com.lhadalo.oladahl.autowork.fragments.DatePickerFragment;
import com.lhadalo.oladahl.autowork.fragments.TimePickerFragment;

import UserPackage.Company;

/**
 * Activity som hanterar tilläggning av arbetspass till applikationen.
 * Arbetspass representerar ett arbetstillfälle på en viss arbetsplats.
 * Användaren kan lägga till arbetsplats, starttid, sluttid, tid för rast, titel och anteckningar.
 * Passet läggs till i databasen, där lön och arbetsade timmar läggs till.
 */
public class AddWorkpassActivity extends AppCompatActivity
        implements AddWorkpassFragment.OnFragmentInteraction, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private AddWorkpassFragment fragment;

    private SQLiteDB database;

    private int requestCode;
    private int dialogSource = 0;

    private Workpass model;
    private List<Company> companies;
    private HashMap<Integer, Company> companyHashMap;
    private Company selectedCompany;
    private GregorianCalendar startDate, startTime, endDate, endTime, startDateTime;

    private List<Workpass> workpasses = new ArrayList<>();

    //----------------------------------------------------------------------------
    // Initierare
    //----------------------------------------------------------------------------

    /**
     * Skapar activity, initierar fragmentet, läser in data från databasen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workpass);

        initFragment();

        database = new SQLiteDB(this);

        requestCode = getIntent().getIntExtra(Tag.REQUEST_CODE, -1);

        companies = database.getAllCompanies();
       // companyHashMap = database.getAllCompaniesHashMap();

    }

    /**
     * Initierar fragmentet som denna activity är kopplad till
     */
    private void initFragment() {
        fragment = new AddWorkpassFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_add_workpass, fragment).commit();
    }

    /**
     * Sätter värden till fragmentet.
     * Ifall ett nytt arbetspass läggs till sätts defaultvärden,
     * ifall ett arbetspass ändras sätts värdet från det valda passet.
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (requestCode > 0) if (requestCode == Tag.ADD_WORKPASS_REQUEST) {
            model = new Workpass();
            selectedCompany = companies.get(0);

            //Om det finns några arbetsplatser sätts det till interface och modell
            if (companies != null) {
                fragment.setCompanyName(selectedCompany.getCompanyName());
                model.setCompanyID(selectedCompany.getCompanyId());
            }
            else {
                fragment.setCompanyName("ERROR!");
            }

            //Hämtar nuvarande tid
            Calendar cal = Calendar.getInstance();

            //Sätter nuvarande tid till interface
            setTimeDate(cal, Tag.START_DATE_TIME);
            setTimeDate(cal, Tag.END_DATE_TIME);

            fragment.setTxtDateStart(formatDate(startDate));
            fragment.setTxtTimeStart(String.valueOf(DateFormat.format("kk:mm", startTime)));

            fragment.setTxtDateEnd(formatDate(endDate));
            fragment.setTxtTimeEnd(String.valueOf(DateFormat.format("kk:mm", endTime)));

            model.setBreaktime(0.0); //Sätter paustid till noll i modellen.


            //Beräknar timmar och lön och sätter i modellen.
            calculateHours();
            setSalary();

            fragment.setBtnSave("Add");
        }
        else if (requestCode == Tag.UPDATE_WORKPASS_REQUEST) {
            //Hämtar arbtespasset som användaren valt.
            model = database.getWorkpass(getIntent().getLongExtra(WorkpassEntry.WORKPASS_ID, -1));

            //Sätter värden från det valda arbetespasset.
            fragment.setTitle(model.getTitle());

            selectedCompany = database.getCompany(model.getCompanyID());
            if (selectedCompany != null) {
                fragment.setCompanyName(selectedCompany.getCompanyName());
            }
            else {
                fragment.setCompanyName("ERROR!"); //Ifall inget company hittades.
            }

            setTimeDate(model.getStartDateTime(), Tag.START_DATE_TIME);
            setTimeDate(model.getEndDateTime(), Tag.END_DATE_TIME);

            fragment.setTxtDateStart(formatDate(startDate));
            fragment.setTxtTimeStart(String.valueOf(DateFormat.format("kk:mm", startTime)));

            fragment.setTxtDateEnd(formatDate(endDate));
            fragment.setTxtTimeEnd(String.valueOf(DateFormat.format("kk:mm", endTime)));

            if (model.getBreaktime() > 0) {
                fragment.setTxtBrake(String.valueOf(model.getBreaktime()));
            }

            if (model.getNote() != null) {
                fragment.setTxtNote(model.getNote());
            }

            fragment.setBtnSave("Update");
        }
    }



    //----------------------------------------------------------------------------
    // Action Events
    //----------------------------------------------------------------------------

    /**
     * Då användaren trycker på arbetsplats i fragment.
     */
    @Override
    public void onClickWorkplace() {
        createCompaniesDialog();
    }

    /**
     * Då användaren trycker på startdatum i fragment.
     * Skapar en ny dialog för att välja datum.
     */
    @Override
    public void onClickDateStart() {
        dialogSource = Tag.START_DATE_TIME;
        DatePickerFragment.newInstance().show(getSupportFragmentManager(), "datepicker");
    }

    /**
     * Då användaren trycker på starttid i fragment.
     * Skapar en ny dialog för att välja tid.
     */
    @Override
    public void onClickTimeStart() {
        dialogSource = Tag.START_DATE_TIME;
        TimePickerFragment.newInstance().show(getSupportFragmentManager(), "timepicker");
    }

    /**
     * Då användaren trycker på slutdatum i fragment.
     * Skapar en ny dialog för att välja datum.
     */
    @Override
    public void onClickDateEnd() {
        dialogSource = Tag.END_DATE_TIME;
        DatePickerFragment.newInstance().show(getSupportFragmentManager(), "datepicker");
    }

    /**
     * Då användaren trycker på sluttid i fragment.
     * Skapar en ny dialog för att välja tid.
     */
    @Override
    public void onClickTimeEnd() {
        dialogSource = Tag.END_DATE_TIME;
        TimePickerFragment.newInstance().show(getSupportFragmentManager(), "timepicker");
    }

    /**
     * Då användaren trycker på rast i fragment.
     * Skapar en ny dialog för att välja tid för rast.
     */
    @Override
    public void onClickBreak() {
        createBreakDialog();
    }

    /**
     * Då användaren trycker på Lägg till eller Uppdatera i fragment.
     * Tittar ifall alla värden är korrekta och lägger till passet till databasen.
     * Activityn avslutas till MainActivity.
     */
    @Override
    public void onClickSave() {

        //Ifall ett nytt pass ska läggas till.
        if (requestCode == Tag.ADD_WORKPASS_REQUEST) {

            //Ifall all information kunde läggas till, läggs modellen till i databasen
            //och activityn avslutas.
            if (populateModelFromInterface()) {
                model.setActionTag(Tag.ON_CREATE_WORKPASS);
                model.setIsSynced(0);
                model.setCompanyServerID(selectedCompany.getServerID());


                AddWorkpassDB.addWorkpass(this, model); //Lägger till passet till databas i nytt thread.

                Intent data = new Intent();
                data.putExtra(WorkpassEntry.MONTH, model.getStartDateTime().get(Calendar.MONTH));

                setResult(RESULT_OK, data);
                finish();
            }
        }
        //Ifall ett arbetspass ska ändras.
        else {
            if (populateModelFromInterface()) {
                if (model.getIsSynced() == 1) {
                    model.setActionTag(Tag.ON_CHANGE_WORKPASS);
                    model.setIsSynced(0);
                }

                model.setCompanyServerID(selectedCompany.getServerID());
                if (database.updateWorkpass(model)) {
                    Intent data = new Intent();
                    data.putExtra(Tag.LIST_POSITION, getIntent().getIntExtra(Tag.LIST_POSITION, -1));
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        }
    }

    /**
     * Då användaren trycker på Avbryt i fragment.
     * Activityn avslutas till MainActivity.
     */
    @Override
    public void onClickCancel() {
        Log.v(Tag.LOGTAG, formatDate(startDateTime));
        //setResult(RESULT_CANCELED);
        //finish();
    }

    /**
     * Då användaren valt ett datum i DatePickerFragment.
     * Ifall startdatum valts sätts startdatum.
     * Ifall slutdatum valts sätts slutdatum.
     *
     * @param datePicker referens till datepicker.
     * @param year det valda året.
     * @param month den valda månaden.
     * @param day den valda dagen.
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if (dialogSource == Tag.START_DATE_TIME) {
            startDate = new GregorianCalendar(year, month, day);
            startDateTime.set(year, month, day);
            fragment.setTxtDateStart(formatDate(startDate));
        }
        else if (dialogSource == Tag.END_DATE_TIME) {
            endDate = new GregorianCalendar(year, month, day);
            fragment.setTxtDateEnd(formatDate(endDate));
        }
    }

    /**
     * Då användaren valt en tid i TimePickerFragment.
     * Ifall starttid valts sätts starttid.
     * Ifall sluttid valts sätts sluttid.
     *
     * @param timePicker referens till timePicker.
     * @param hour den valda timmen.
     * @param minute den valda minuten.
     */
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        if (dialogSource == Tag.START_DATE_TIME) {
            fragment.setTxtTimeStart(String.valueOf(DateFormat.format("kk:mm",
                    new GregorianCalendar(0, 0, 0, hour, minute))));
            startTime = new GregorianCalendar(0, 0, 0, hour, minute);

            startDateTime.set(Calendar.HOUR_OF_DAY, hour);
            startDateTime.set(Calendar.MINUTE, minute);

            calculateHours();
            setSalary();
        }
        else if (dialogSource == Tag.END_DATE_TIME) {
            fragment.setTxtTimeEnd(String.valueOf(DateFormat.format("kk:mm",
                    new GregorianCalendar(0, 0, 0, hour, minute))));
            endTime = new GregorianCalendar(0, 0, 0, hour, minute);
            calculateHours();
            setSalary();
        }
    }

    /**
     * Sätter datum och tid till instansvariabler för tid och datum.
     *
     * @param cal tiden som ska sättas.
     * @param dateTimeTarget ifall det är starttid eller sluttid som ska sättas.
     */
    private void setTimeDate(Calendar cal, int dateTimeTarget) {

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = 12;
        int minute = 0;

        switch (dateTimeTarget) {
            case Tag.START_DATE_TIME:
                startDate = new GregorianCalendar(year, month, dayOfMonth);
                startTime = new GregorianCalendar(0, 0, 0, hourOfDay, minute);
                startDateTime = new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute);
                break;
            case Tag.END_DATE_TIME:
                //Ifall ett nytt pass läggs till ska 3 timmar läggas till sluttiden
                if (requestCode == Tag.ADD_WORKPASS_REQUEST) {
                    endDate = new GregorianCalendar(year, month, dayOfMonth);
                    endTime = new GregorianCalendar(0, 0, 0, (hourOfDay + 3), minute);
                }
                else {
                    endDate = new GregorianCalendar(year, month, dayOfMonth);
                    endTime = new GregorianCalendar(0, 0, 0, hourOfDay, minute);
                }
                break;
        }
    }

    /**
     * Slår ihop det valda datumet med de
      * @param date
     * @param time
     * @return
     */
    private GregorianCalendar joinDateTime(GregorianCalendar date, GregorianCalendar time) {
        return new GregorianCalendar(
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH),
                time.get(Calendar.HOUR_OF_DAY),
                time.get(Calendar.MINUTE));
    }


    private boolean populateModelFromInterface() {
        if (validateTitle() && validateDateTime()) {
            User user = database.getUser();

            model.setUserId(user.getUserid());
            model.setCompanyID(selectedCompany.getCompanyId());
            model.setNote(fragment.getNote());
            return true;
        }
        else {
            return false;
        }
    }

    private boolean validateTitle() {
        String res = fragment.getTitle();
        if (res.isEmpty()) {
            createAlertDialog("No title", "The workpass must have a title");
            return false;
        }
        else {
            model.setTitle(res);
            return true;
        }
    }

    private boolean validateDateTime() {
        if (startDate.compareTo(endDate) > 0) {
            createAlertDialog("Wrong date", "The end date cannot be before the start date");
            return false;
        }
        if (startTime.compareTo(endTime) == 0) {
            createAlertDialog("Same time", "You can't have the same start time as your end time");
            return false;
        }
        if (startTime.compareTo(endTime) > 0) {
            createAlertDialog("Wrong time", "The end time cannot be before the start time");
            return false;
        }
        else {
            GregorianCalendar start = joinDateTime(startDate, startTime);
            model.setStartDateTime(start);

            GregorianCalendar end = joinDateTime(endDate, endTime);
            model.setEndDateTime(end);

            return true;
        }
    }

    //TODO Fixa så det fungerar med tillägning av pass mellan månader
    private void calculateHours() {


        //Konverterar tid till timmar
        float startTimeHours = (((float)startTime.get(Calendar.HOUR_OF_DAY) * 60) +
                (float)startTime.get(Calendar.MINUTE)) / 60;
        float endTimeHours =(((float)endTime.get(Calendar.HOUR_OF_DAY) * 60) +
                (float)endTime.get(Calendar.MINUTE)) / 60;

        double breaktime = (model.getBreaktime() / 60);
        double nbrOfHours;


        //Ifall startdatum och slutdatum ej är samma ska tiden mellan datum hämtas.
        if(startDate.get(Calendar.DATE) != endDate.get(Calendar.DATE)){

            //Antalet timmarna för startdatumet
            float firstDayHours = 24 - (startTimeHours + (float)breaktime);

            //Dagarna mellan nästa dag och slutdatumet
            float dateDifferenceStart = startDate.get(Calendar.DATE) + 1;
            float dateDifferenceEnd = endDate.get(Calendar.DATE);
            float dateDiff = dateDifferenceEnd - dateDifferenceStart;

            //Antalet timmar för dagarna mellan
            float dayHours = 24 * dateDiff;

            //Lägger ihop alla timmar
            nbrOfHours = firstDayHours + dayHours + endTimeHours;
        }
        //Annars sätts bara en dags arbetade timmar
        else{
            nbrOfHours = endTimeHours - startTimeHours;
        }

        //Sätter antal timmar till datamodellen.
        model.setWorkingHours(nbrOfHours);
    }

    private void setSalary() {
        double hourlyWage = selectedCompany.getHourlyWage();
        double salary = model.getWorkingHours() * hourlyWage;
        model.setSalary(salary);
    }


    private String formatDate(GregorianCalendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        return getResources().getStringArray(R.array.months)[month] + " " + dayOfMonth + ", " + year;
    }

    private String formatDate(int year, int month, int dayOfMonth) {
        return getResources().getStringArray(R.array.months)[month] + " " + dayOfMonth + ", " + year;
    }

    //----------------------------------------------------------------------------
    // Dialoger
    //----------------------------------------------------------------------------

    private void createAlertDialog(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private void createBreakDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add brake in minutes");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        try {
                            String brakeTime = input.getText().toString();
                            model.setBreaktime(Integer.parseInt(brakeTime));
                            fragment.setTxtBrake(String.valueOf(brakeTime + " min"));
                        } catch (NumberFormatException e) {
                            createAlertDialog("Fel input", "Någonting blev fel med input");
                        }
                    }
                }
        ).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void createCompaniesDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Choose a workplace");

        final String[] companyStrings = new String[companies.size() + 1];
        for (int i = 0; i < companies.size(); i++) {
            companyStrings[i] = companies.get(i).getCompanyName();
        }
        companyStrings[companyStrings.length - 1] = "Add Company";


        dialog.setItems(companyStrings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                if (position == companyStrings.length - 1) {

                    Intent intent = new Intent(AddWorkpassActivity.this, AddCompanyActivity.class);
                    startActivity(intent);


                }
                else {
                    selectedCompany = companies.get(position);
                }

                fragment.setCompanyName(selectedCompany.getCompanyName());
            }
        });

        dialog.show();
    }
}
