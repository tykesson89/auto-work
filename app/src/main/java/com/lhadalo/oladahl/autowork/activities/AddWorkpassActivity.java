package com.lhadalo.oladahl.autowork.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
    private Company selectedCompany;

    private GregorianCalendar startDateTime, endDateTime;

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
        companies = database.getAllCompanies();

        requestCode = getIntent().getIntExtra(Tag.REQUEST_CODE, -1);
        if (requestCode == Tag.ADD_WORKPASS_REQUEST) {
            model = new Workpass();
            selectedCompany = companies.get(0);

            GregorianCalendar now = (GregorianCalendar)Calendar.getInstance(Locale.getDefault());
            startDateTime = (GregorianCalendar)now.clone();

            now.add(Calendar.HOUR_OF_DAY, 3);
            endDateTime = (GregorianCalendar)now.clone();

            model.setCompanyID(selectedCompany.getCompanyId());
            model.setStartDateTime(startDateTime);
            model.setEndDateTime(endDateTime);


            //Beräknar timmar och lön och sätter i modellen.
            calculateHours();
            setSalary();

        }
        else if (requestCode == Tag.UPDATE_WORKPASS_REQUEST) {
            //Hämtar arbtespasset som användaren valt.
            model = database.getWorkpass(getIntent().getLongExtra(WorkpassEntry.WORKPASS_ID, -1));

            selectedCompany = database.getCompany(model.getCompanyID());
            startDateTime = model.getStartDateTime();
            endDateTime = model.getEndDateTime();
        }
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

        if (requestCode > 0)
            if (requestCode == Tag.ADD_WORKPASS_REQUEST) {

                //Om det finns några arbetsplatser sätts det till interface och modell
                if (companies != null) {
                    fragment.setCompanyName(selectedCompany.getCompanyName());
                }
                else {
                    fragment.setCompanyName("ERROR!");
                }

                fragment.setTxtDateStart(formatDate(startDateTime));
                fragment.setTxtTimeStart(formatTime(startDateTime));

                fragment.setTxtDateEnd(formatDate(endDateTime));
                fragment.setTxtTimeEnd(formatTime(endDateTime));

                fragment.setBtnSave("Add");


            }
            else if (requestCode == Tag.UPDATE_WORKPASS_REQUEST) {

                //Sätter värden från det valda arbetespasset.
                fragment.setTitle(model.getTitle());

                if (selectedCompany != null) {
                    fragment.setCompanyName(selectedCompany.getCompanyName());
                }
                else {
                    fragment.setCompanyName("ERROR!"); //Ifall inget company hittades.
                }

                fragment.setTxtDateStart(formatDate(startDateTime));
                fragment.setTxtTimeStart(formatTime(startDateTime));

                fragment.setTxtDateEnd(formatDate(endDateTime));
                fragment.setTxtTimeEnd(formatTime(endDateTime));

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

                AddWorkpassDB.addWorkpass(this, model); //Lägger till passet till databas i nytt thread.

                Intent data = new Intent();
                data.putExtra(WorkpassEntry.MONTH, startDateTime.get(Calendar.MONTH));

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
                else {
                    model.setActionTag(Tag.ON_CREATE_COMPANY);
                    model.setIsSynced(Tag.IS_NOT_SYNCED);
                }

                model.setCompanyServerID(selectedCompany.getServerID());
                if (database.updateWorkpass(model)) {
                    Intent data = new Intent();
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        }
    }

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
        DatePickerFragment datePicker = new DatePickerFragment();

        Bundle args = new Bundle(3);

        args.putInt(Tag.YEAR, startDateTime.get(Calendar.YEAR));
        args.putInt(Tag.MONTH, startDateTime.get(Calendar.MONTH));
        args.putInt(Tag.DAY, startDateTime.get(Calendar.DAY_OF_MONTH));

        datePicker.setArguments(args);

        datePicker.show(getSupportFragmentManager(), "datepicker");
    }

    /**
     * Då användaren trycker på starttid i fragment.
     * Skapar en ny dialog för att välja tid.
     */
    @Override
    public void onClickTimeStart() {
        dialogSource = Tag.START_DATE_TIME;

        TimePickerFragment timePicker = new TimePickerFragment();
        Bundle bundle = new Bundle(2);
        bundle.putInt(Tag.HOUR, startDateTime.get(Calendar.HOUR_OF_DAY));
        bundle.putInt(Tag.MINUTE, startDateTime.get(Calendar.MINUTE));

        timePicker.setArguments(bundle);

        timePicker.show(getSupportFragmentManager(), "timepicker");

    }

    /**
     * Då användaren trycker på slutdatum i fragment.
     * Skapar en ny dialog för att välja datum.
     */
    @Override
    public void onClickDateEnd() {
        dialogSource = Tag.END_DATE_TIME;
        DatePickerFragment datePicker = new DatePickerFragment();
        Bundle args = new Bundle(3);

        args.putInt(Tag.YEAR, endDateTime.get(Calendar.YEAR));
        args.putInt(Tag.MONTH, endDateTime.get(Calendar.MONTH));
        args.putInt(Tag.DAY, endDateTime.get(Calendar.DAY_OF_MONTH));

        datePicker.setArguments(args);

        datePicker.show(getSupportFragmentManager(), "datepicker");

    }

    /**
     * Då användaren trycker på sluttid i fragment.
     * Skapar en ny dialog för att välja tid.
     */
    @Override
    public void onClickTimeEnd() {
        dialogSource = Tag.END_DATE_TIME;

        TimePickerFragment timePicker = new TimePickerFragment();
        Bundle bundle = new Bundle(2);
        bundle.putInt(Tag.HOUR, endDateTime.get(Calendar.HOUR_OF_DAY));
        bundle.putInt(Tag.MINUTE, endDateTime.get(Calendar.MINUTE));

        timePicker.setArguments(bundle);

        timePicker.show(getSupportFragmentManager(), "timepicker");
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
     * Då användaren trycker på Avbryt i fragment.
     * Activityn avslutas till MainActivity.
     */
    @Override
    public void onClickCancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Då användaren valt ett datum i DatePickerFragment.
     * Ifall startdatum valts sätts startdatum.
     * Ifall slutdatum valts sätts slutdatum.
     *
     * @param datePicker referens till datepicker.
     * @param year       det valda året.
     * @param month      den valda månaden.
     * @param day        den valda dagen.
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if (dialogSource == Tag.START_DATE_TIME) {
            startDateTime.set(year, month, day);
            fragment.setTxtDateStart(formatDate(startDateTime));
        }
        else if (dialogSource == Tag.END_DATE_TIME) {
            endDateTime.set(year, month, day);
            fragment.setTxtDateEnd(formatDate(endDateTime));
        }
    }

    /**
     * Då användaren valt en tid i TimePickerFragment.
     * Ifall starttid valts sätts starttid.
     * Ifall sluttid valts sätts sluttid.
     *
     * @param timePicker referens till timePicker.
     * @param hour       den valda timmen.
     * @param minute     den valda minuten.
     */
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        if (dialogSource == Tag.START_DATE_TIME) {
            startDateTime.set(Calendar.HOUR_OF_DAY, hour);
            startDateTime.set(Calendar.MINUTE, minute);

            fragment.setTxtTimeStart(formatTime(startDateTime));
            calculateHours();
            setSalary();
        }
        else if (dialogSource == Tag.END_DATE_TIME) {

            endDateTime.set(Calendar.HOUR_OF_DAY, hour);
            endDateTime.set(Calendar.MINUTE, minute);


            fragment.setTxtTimeEnd(formatTime(endDateTime));
            calculateHours();
            setSalary();
        }


    }

    private String formatDate(GregorianCalendar calendar) {
        /*int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        getResources().getStringArray(R.array.months)[month] + " " + dayOfMonth + ", " + year;*/
        SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        return format.format(calendar.getTime());
    }

    private String formatTime(GregorianCalendar calendar){
        SimpleDateFormat format = new SimpleDateFormat("kk:mm", Locale.getDefault());
        return format.format(calendar.getTime());
    }

    private boolean populateModelFromInterface() {
        if (validateTitle() && validateDateTime()) {

            model.setStartDateTime(startDateTime);
            model.setEndDateTime(endDateTime);

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
        GregorianCalendar start = startDateTime;
        GregorianCalendar end = endDateTime;

        //För att jag bara vill jämföra datum först.
        GregorianCalendar startDate = new GregorianCalendar(
                start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH));

        GregorianCalendar endDate = new GregorianCalendar(
                end.get(Calendar.YEAR), end.get(Calendar.MONTH), end.get(Calendar.DAY_OF_MONTH));

        Log.v(Tag.LOGTAG, startDate.toString());

        if (startDate.compareTo(endDate) > 0) {
            createAlertDialog("Wrong date", "The end date cannot be before the start date");
            return false;
        }
        if (startDateTime.compareTo(endDateTime) == 0) {
            createAlertDialog("Same time", "You can't have the same start time as your end time");
            return false;
        }
        if (startDateTime.compareTo(endDateTime) > 0) {
            createAlertDialog("Wrong time", "The end time cannot be before the start time");
            return false;
        }
        else {
            //Allting stämmer.
            return true;
        }
    }

    //TODO Fixa så det fungerar med tillägning av pass mellan månader
    private void calculateHours() {
        //Konverterar tid till timmar
        float startTimeHours = (((float)startDateTime.get(Calendar.HOUR_OF_DAY) * 60) +
                (float)startDateTime.get(Calendar.MINUTE)) / 60;
        float endTimeHours = (((float)endDateTime.get(Calendar.HOUR_OF_DAY) * 60) +
                (float)endDateTime.get(Calendar.MINUTE)) / 60;

        double breaktime = (model.getBreaktime() / 60);
        double nbrOfHours;


        //Ifall startdatum och slutdatum ej är samma ska tiden mellan datum hämtas.
        if (startDateTime.get(Calendar.DATE) != endDateTime.get(Calendar.DATE)) {

            //Antalet timmarna för startdatumet
            float firstDayHours = 24 - (startTimeHours + (float)breaktime);

            //Dagarna mellan nästa dag och slutdatumet
            float dateDifferenceStart = startDateTime.get(Calendar.DATE) + 1; //TODO Lite konstiga namn på detta.
            float dateDifferenceEnd = endDateTime.get(Calendar.DATE);
            float dateDiff = dateDifferenceEnd - dateDifferenceStart;

            //Antalet timmar för dagarna mellan
            float dayHours = 24 * dateDiff;

            //Lägger ihop alla timmar
            nbrOfHours = firstDayHours + dayHours + endTimeHours;
        }
        //Annars sätts bara en dags arbetade timmar
        else {
            nbrOfHours = endTimeHours - startTimeHours;
        }

        //Sätter antal timmar till datamodellen.
        nbrOfHours = Math.round(nbrOfHours); //Avrundar värdet.
        model.setWorkingHours(nbrOfHours);
    }

    private void setSalary() {
        double hourlyWage = selectedCompany.getHourlyWage();
        double salary = model.getWorkingHours() * hourlyWage;
        model.setSalary(salary);
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

                    Intent intent = new Intent(AddWorkpassActivity.this, AddCompanySettingsActivity.class);
                    intent.putExtra(Tag.REQUEST_CODE, Tag.ADD_COMPANY_REQUEST);
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
