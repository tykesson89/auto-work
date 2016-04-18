package com.lhadalo.oladahl.autowork.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.SQLiteDB;
import com.lhadalo.oladahl.autowork.Tag;

import UserPackage.User;
import UserPackage.WorkpassModel;

import com.lhadalo.oladahl.autowork.WorkpassContract.WorkpassEntry;
import com.lhadalo.oladahl.autowork.fragments.AddWorkpassFragment;

import UserPackage.Company;

public class AddWorkpassActivity extends AppCompatActivity
        implements AddWorkpassFragment.OnFragmentInteraction, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private AddWorkpassFragment fragment;

    private SQLiteDB database;

    private int requestCode;
    private int dialogSource = 0;

    private WorkpassModel model;
    private List<Company> companies;
    private Company selectedCompany;
    private GregorianCalendar startDate, startTime, endDate, endTime;

    private List<WorkpassModel> workpassModels = new ArrayList<>();

    //----------------------------------------------------------------------------
    // Initierare
    //----------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workpass);

        initFragment();

        database = new SQLiteDB(this);

        companies = database.getAllCompanies();
    }

    private void initFragment() {
        fragment = new AddWorkpassFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_add_workpass, fragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        requestCode = getIntent().getIntExtra(Tag.REQUEST_CODE, -1);

        if(requestCode > 0) {
            if(requestCode == Tag.ADD_WORKPASS_REQUEST) {
                model = new WorkpassModel();
                selectedCompany = companies.get(0);

                //Om det finns några arbetsplatser sätts det till interface och modell
                if(companies != null) {
                    fragment.setCompanyName(selectedCompany.getCompanyName());
                    model.setCompany(selectedCompany);
                }
                else {
                    fragment.setCompanyName("ERROR!");
                }

                //Hämtar nuvarande tid
                Calendar cal = Calendar.getInstance();

                //Sätter tid till interface
                setTimeDate(cal, Tag.START_DATE_TIME);
                setTimeDate(cal, Tag.END_DATE_TIME);

                fragment.setTxtDateStart(formatDate(startDate));
                fragment.setTxtTimeStart(String.valueOf(DateFormat.format("kk:mm", startTime)));

                fragment.setTxtDateEnd(formatDate(endDate));
                fragment.setTxtTimeEnd(String.valueOf(DateFormat.format("kk:mm", endTime)));

                //Sätter paustid till noll i modellen.
                model.setBreaktime(0.0);

                //Beräknar timmar och lön och sätter i modellen.
                calculateHours();
                setSalary();

                fragment.setBtnSave("Add");

            }
            else if(requestCode == Tag.UPDATE_WORKPASS_REQUEST) {
                model = database.getWorkpass(getIntent().getLongExtra(WorkpassEntry.WORKPASS_ID, -1));

                //Sätta alla fält från modellen
                fragment.setTitle(model.getTitle());

                selectedCompany = model.getCompany();
                fragment.setCompanyName(selectedCompany.getCompanyName());

                setTimeDate(model.getStartDateTime(), Tag.START_DATE_TIME);
                setTimeDate(model.getEndDateTime(), Tag.END_DATE_TIME);

                fragment.setTxtDateStart(formatDate(startDate));
                fragment.setTxtTimeStart(String.valueOf(DateFormat.format("kk:mm", startTime)));

                fragment.setTxtDateEnd(formatDate(endDate));
                fragment.setTxtTimeEnd(String.valueOf(DateFormat.format("kk:mm", endTime)));

                if(model.getBreaktime() > 0){
                    fragment.setTxtBrake(String.valueOf(model.getBreaktime()));
                }

                if(model.getNote() != null){
                    fragment.setTxtNote(model.getNote());
                }

                fragment.setBtnSave("Update");
            }
        }
    }

    private void setTimeDate(Calendar cal, int dateTimeTarget) {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        switch(dateTimeTarget) {
            case Tag.START_DATE_TIME:
                startDate = new GregorianCalendar(year, month, dayOfMonth);
                startTime = new GregorianCalendar(0, 0, 0, hourOfDay, minute);
                break;
            case Tag.END_DATE_TIME:
                //Ifall ett nytt pass läggs till ska 3 timmar läggas till sluttiden
                if(requestCode == Tag.ADD_WORKPASS_REQUEST) {
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

    //----------------------------------------------------------------------------
    // Action Events
    //----------------------------------------------------------------------------

    @Override
    public void onClickWorkplace() {
        createCompaniesDialog();
    }

    @Override
    public void onClickDateStart() {
        dialogSource = Tag.START_DATE_TIME;
        DatePickerFragment.newInstance().show(getSupportFragmentManager(), "datepicker");
    }

    @Override
    public void onClickTimeStart() {
        dialogSource = Tag.START_DATE_TIME;
        TimePickerFragment.newInstance().show(getSupportFragmentManager(), "timepicker");
    }

    @Override
    public void onClickDateEnd() {
        dialogSource = Tag.END_DATE_TIME;
        DatePickerFragment.newInstance().show(getSupportFragmentManager(), "datepicker");
    }

    @Override
    public void onClickTimeEnd() {
        dialogSource = Tag.END_DATE_TIME;
        TimePickerFragment.newInstance().show(getSupportFragmentManager(), "timepicker");
    }

    @Override
    public void onClickBreak() {
        createBreakDialog();
    }

    @Override
    public void onClickSave() {
        if(requestCode == Tag.ADD_WORKPASS_REQUEST) {
            //Ifall all information kunde läggas till, läggs modellen till i databasen
            //och activityn avslutas.
            if(populateModelFromInterface()) {
                long id = database.addWorkpass(model);
                model.setId(id);

                Intent data = new Intent();
                data.putExtra(WorkpassEntry.WORKPASS_ID, id);

                setResult(RESULT_OK, data);
                finish();
            }
        }
        else {
            if(populateModelFromInterface()){
                if(database.updateWorkpass(model)){
                    Intent data = new Intent();
                    data.putExtra(Tag.LIST_POSITION, getIntent().getIntExtra(Tag.LIST_POSITION, -1));
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        }
    }

    @Override
    public void onClickCancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if(dialogSource == Tag.START_DATE_TIME) {
            fragment.setTxtDateStart(formatDate(year, month, day));
            startDate = new GregorianCalendar(year, month, day);
        }
        else if(dialogSource == Tag.END_DATE_TIME) {
            fragment.setTxtDateEnd(formatDate(year, month, day));
            endDate = new GregorianCalendar(year, month, day);
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        if(dialogSource == Tag.START_DATE_TIME) {
            fragment.setTxtTimeStart(String.valueOf(DateFormat.format("kk:mm",
                    new GregorianCalendar(0, 0, 0, hour, minute))));
            startTime = new GregorianCalendar(0, 0, 0, hour, minute);
            calculateHours();
            setSalary();
        }
        else if(dialogSource == Tag.END_DATE_TIME) {
            fragment.setTxtTimeEnd(String.valueOf(DateFormat.format("kk:mm",
                    new GregorianCalendar(0, 0, 0, hour, minute))));
            endTime = new GregorianCalendar(0, 0, 0, hour, minute);
            calculateHours();
            setSalary();
        }
    }

    //----------------------------------------------------------------------------
    // Logik
    //----------------------------------------------------------------------------

    private boolean populateModelFromInterface() {
        if(validTitle() && validDateTime()) {
            User user = database.getUser();

            model.setUserId(user.getUserid());
            model.setNote(fragment.getNote());
            return true;
        }
        else {
            return false;
        }
    }

    private boolean validTitle() {
        String res = fragment.getTitle();
        if(res.isEmpty()) {
            createAlertDialog("No title", "The workpass must have a title");
            return false;
        }
        else {
            model.setTitle(res);
            return true;
        }
    }

    private boolean validDateTime() {
        if(startDate.compareTo(endDate) > 0) {
            createAlertDialog("Wrong date", "The end date cannot be before the start date");
            return false;
        }
        if(startTime.compareTo(endTime) == 0) {
            createAlertDialog("Same time", "You can't have the same start time as your end time");
            return false;
        }
        if(startTime.compareTo(endTime) > 0) {
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

    private void calculateHours() {
        double start = (startTime.get(Calendar.HOUR_OF_DAY) * 60) + startTime.get(Calendar.MINUTE);
        double end = (endTime.get(Calendar.HOUR_OF_DAY) * 60) + endTime.get(Calendar.MINUTE);

        double breaktime = model.getBreaktime();
        double difference = end - (start + breaktime);

        double workingTime = difference / 60;

        model.setWorkingHours(workingTime);
    }

    private void setSalary() {
        double hourlyWage = selectedCompany.getHourlyWage();
        double salary = model.getWorkingHours() * hourlyWage;
        model.setSalary(salary);
    }

    private GregorianCalendar joinDateTime(GregorianCalendar date, GregorianCalendar time) {
        return new GregorianCalendar(
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH),
                time.get(Calendar.HOUR_OF_DAY),
                time.get(Calendar.MINUTE));
    }

    private String formatDate(GregorianCalendar calendar){
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

        final String[] companyStrings = new String[companies.size()+1];
        for(int i = 0; i < companies.size(); i++) {
            companyStrings[i] = companies.get(i).getCompanyName();
        }
        companyStrings[companyStrings.length-1] = "Add Company";


        dialog.setItems(companyStrings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                if(position == companyStrings.length-1){

                    Intent intent = new Intent(AddWorkpassActivity.this, AddCompanyActivity.class);
                    startActivity(intent);





                }
                else
                    selectedCompany = companies.get(position);
                model.setCompany(selectedCompany);
                fragment.setCompanyName(selectedCompany.getCompanyName());
            }
        });

        dialog.show();
    }

    public static class TimePickerFragment extends DialogFragment {
        private TimePickerDialog.OnTimeSetListener listener;

        public static TimePickerFragment newInstance() {
            return new TimePickerFragment();
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            listener = (TimePickerDialog.OnTimeSetListener)context;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), listener, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }
    }

    public static class DatePickerFragment extends DialogFragment {
        private DatePickerDialog.OnDateSetListener listener;

        public static DatePickerFragment newInstance() {
            return new DatePickerFragment();
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            listener = (DatePickerDialog.OnDateSetListener)context;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), listener, year, month, day);
        }
    }
}
