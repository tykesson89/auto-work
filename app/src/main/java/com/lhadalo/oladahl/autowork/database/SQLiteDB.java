package com.lhadalo.oladahl.autowork.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;


import UserPackage.Company;
import UserPackage.User;
import UserPackage.Workpass;

import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.database.DatabaseContract.WorkpassEntry;
import com.lhadalo.oladahl.autowork.database.DatabaseContract.UserEntry;
import com.lhadalo.oladahl.autowork.database.DatabaseContract.CompanyEntry;
import com.lhadalo.oladahl.autowork.database.SQLiteCommand;

/**
 * Created by Henrik on 2016-02-29.
 */
public class SQLiteDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AutoWork_DB";

    public SQLiteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLiteCommand.DB_CREATE_USER_TABLE);
        Log.d("User Table", "created");

        db.execSQL(SQLiteCommand.DB_CREATE_COMPANY_TABLE);
        Log.d("Company Table", "created");

        db.execSQL(SQLiteCommand.DB_CREATE_WORKPASS_TABLE);
        Log.d("Workpass Table", "created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CompanyEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WorkpassEntry.TABLE_NAME);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UserEntry.TABLE_NAME, null, null);
        db.delete(CompanyEntry.TABLE_NAME, null, null);
        db.delete(WorkpassEntry.TABLE_NAME, null, null);
        Log.d("Database: ", "Deleted");
        onCreate(db);
        db.close();
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        String firstname = user.getFirstname();
        String lastname = user.getLastname();
        String email = user.getEmail();
        int id = user.getUserid();

        ContentValues data = new ContentValues();
        data.put(UserEntry.FIRST_NAME, firstname);
        data.put(UserEntry.LAST_NAME, lastname);
        data.put(UserEntry.EMAIL, email);

        db.update(UserEntry.TABLE_NAME, data, UserEntry.USER_ID + "=" + id, null);
        db.close();


    }

    public boolean loginUser(User user) {
        int userid = user.getUserid();
        String firstname = user.getFirstname();
        String lastname = user.getLastname();
        String email = user.getEmail();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(UserEntry.USER_ID, userid);
        content.put(UserEntry.EMAIL, email);
        content.put(UserEntry.FIRST_NAME, firstname);
        content.put(UserEntry.LAST_NAME, lastname);

        db.insert(UserEntry.TABLE_NAME, null, content);
        db.close();
        return true;
    }

    //Company---------------------------------------------------------------------------------
    public long addCompany(Company company) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(CompanyEntry.USER_ID, company.getUserId());
        content.put(CompanyEntry.COMPANY_MY_SQL_ID, company.getServerID());
        content.put(CompanyEntry.COMPANY_NAME, company.getCompanyName());
        content.put(CompanyEntry.WAGE, company.getHourlyWage());
        content.put(CompanyEntry.IS_SYNCED, company.getIsSynced());
        content.put(CompanyEntry.ACTION_TAG, company.getActionTag());

        return db.insert(CompanyEntry.TABLE_NAME, null, content);
    }

    public double getHourlyWage(String companyName) {
        SQLiteDatabase db = this.getReadableDatabase();


        String query = "select " + CompanyEntry.WAGE + " from " + CompanyEntry.TABLE_NAME +
                " where " + CompanyEntry.COMPANY_NAME + "=?";

        Cursor c = db.rawQuery(query, new String[]{companyName});

        c.moveToFirst();
        double hourlyWage = c.getDouble(0);
        return hourlyWage;

    }

    public List<Company> getAllCompanies() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CompanyEntry.TABLE_NAME, null);
        List<Company> companies = new ArrayList<>();

        while (cursor.moveToNext()) {
            Company company = new Company(
                    cursor.getLong(cursor.getColumnIndex(CompanyEntry.COMPANY_ID)),
                    cursor.getInt(cursor.getColumnIndex(CompanyEntry.COMPANY_MY_SQL_ID)),
                    cursor.getInt(cursor.getColumnIndex(CompanyEntry.USER_ID)),
                    cursor.getString(cursor.getColumnIndex(CompanyEntry.COMPANY_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(CompanyEntry.WAGE)),
                    cursor.getInt(cursor.getColumnIndex(CompanyEntry.IS_SYNCED)),
                    cursor.getString(cursor.getColumnIndex(CompanyEntry.ACTION_TAG))
            );

            companies.add(company);
        }

        db.close();
        cursor.close();

        return companies;
    }

    public long getLocalCompanyId(Workpass workpass){
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "select "
                + CompanyEntry.COMPANY_ID
                + " from " + CompanyEntry.TABLE_NAME
                + " where " + CompanyEntry.COMPANY_MY_SQL_ID + "=?";


        Cursor cursor = db.rawQuery(select,
                new String[]{String.valueOf(workpass.getCompanyServerID())});

        cursor.moveToFirst();
        return cursor.getLong(cursor.getColumnIndex(CompanyEntry.COMPANY_ID));
    }

    public HashMap<Integer, Company> getAllCompaniesHashMap() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CompanyEntry.TABLE_NAME, null);

        HashMap<Integer, Company> companies = new HashMap<>();

        while (cursor.moveToNext()) {
            Company company = new Company(
                    cursor.getLong(cursor.getColumnIndex(CompanyEntry.COMPANY_ID)),
                    cursor.getInt(cursor.getColumnIndex(CompanyEntry.COMPANY_MY_SQL_ID)),
                    cursor.getInt(cursor.getColumnIndex(CompanyEntry.USER_ID)),
                    cursor.getString(cursor.getColumnIndex(CompanyEntry.COMPANY_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(CompanyEntry.WAGE)),
                    cursor.getInt(cursor.getColumnIndex(CompanyEntry.IS_SYNCED)),
                    cursor.getString(cursor.getColumnIndex(CompanyEntry.ACTION_TAG))
            );

            companies.put((int) company.getCompanyId(), company);
        }

        db.close();
        cursor.close();

        return companies;
    }

    public Company getCompany(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + CompanyEntry.TABLE_NAME + " where "
                + CompanyEntry.COMPANY_ID + "=" + id, null);


        cursor.moveToFirst();
        Company company = new Company(
                cursor.getLong(cursor.getColumnIndex(CompanyEntry.COMPANY_ID)),
                cursor.getInt(cursor.getColumnIndex(CompanyEntry.COMPANY_MY_SQL_ID)),
                cursor.getInt(cursor.getColumnIndex(CompanyEntry.USER_ID)),
                cursor.getString(cursor.getColumnIndex(CompanyEntry.COMPANY_NAME)),
                cursor.getDouble(cursor.getColumnIndex(CompanyEntry.WAGE)),
                cursor.getInt(cursor.getColumnIndex(CompanyEntry.IS_SYNCED)),
                cursor.getString(cursor.getColumnIndex(CompanyEntry.ACTION_TAG))
        );

        db.close();
        cursor.close();

        return company;
    }


    //Company---------------------------------------------------------------------------------

    public User getUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        User user;
        Cursor c = db.rawQuery("SELECT * FROM " + UserEntry.TABLE_NAME, null);

        c.moveToFirst();
        int userID = c.getInt(c.getColumnIndex(UserEntry.USER_ID));
        String firstName = c.getString(c.getColumnIndex(UserEntry.FIRST_NAME));
        String lastName = c.getString(c.getColumnIndex(UserEntry.LAST_NAME));
        String email = c.getString(c.getColumnIndex(UserEntry.EMAIL));
        user = new User(firstName, lastName, email, userID);
        db.close();
        return user;
    }

    public Boolean isLoggedIn() {
        String countQuery = "SELECT * FROM " + UserEntry.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        db.close();

        if (cnt == 0) {
            return false;
        }
        else {
            return true;
        }

    }

    public void changeCompany(Company company) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CompanyEntry.WAGE, company.getHourlyWage());
        values.put(CompanyEntry.IS_SYNCED, company.getIsSynced());
        values.put(CompanyEntry.ACTION_TAG, company.getActionTag());
        values.put(CompanyEntry.COMPANY_MY_SQL_ID, company.getServerID());

        db.update(CompanyEntry.TABLE_NAME, values, CompanyEntry.COMPANY_ID + "=" + company.getCompanyId(), null);

        db.close();
    }

    public void deleteCompany(String companyName) {
        {
            SQLiteDatabase db = this.getWritableDatabase();
            try {
                db.delete(CompanyEntry.TABLE_NAME, CompanyEntry.COMPANY_NAME + "=?", new String[]{companyName});
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
    }

    //Workpass-----------------------------------------------------------------------------------
    public long addWorkpass(Workpass model) {
        ContentValues values = populateContentValuesFromModel(model);
        return this.getWritableDatabase().insert(WorkpassEntry.TABLE_NAME, null, values);
    }

    public List<Workpass> getAllWorkpasses() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + WorkpassEntry.TABLE_NAME + " where not "
                        + WorkpassEntry.ACTION_TAG + "=?", new String[]{Tag.ON_DELETE_WORKPASS});
        List<Workpass> workpasses = new ArrayList<>();

        while (cursor.moveToNext()) {
            Workpass model = populateModelFromCursor(cursor);

            workpasses.add(model);
        }

        db.close();

        return workpasses;
    }

    public Workpass getWorkpass(long id) {
        String command = "SELECT * FROM " + WorkpassEntry.TABLE_NAME + " WHERE "
                + WorkpassEntry.WORKPASS_ID + "=" + String.valueOf(id);

        Cursor c = this.getReadableDatabase().rawQuery(command, null);

        c.moveToFirst();

        return this.populateModelFromCursor(c);
    }

    public Workpass getLastAddedWorkpass() {
        String command = "SELECT * FROM " + WorkpassEntry.TABLE_NAME + " WHERE " + WorkpassEntry.WORKPASS_ID
                + " = (SELECT MAX(" + WorkpassEntry.WORKPASS_ID + ") FROM " + WorkpassEntry.TABLE_NAME + ");";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(command, null);
        c.moveToFirst();
        Workpass model = populateModelFromCursor(c);

        c.close();
        db.close();

        return model;
    }

    public List<Workpass> getWorkpassMonth(int month) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + WorkpassEntry.TABLE_NAME
                + " WHERE " + WorkpassEntry.MONTH + "=?", new String[]{String.valueOf(month)});



        List<Workpass> workpasses = new ArrayList<>();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Workpass model = populateModelFromCursor(cursor);
                if (!model.getActionTag().equals(Tag.ON_DELETE_WORKPASS)) {
                    workpasses.add(model);
                }
            }

            return workpasses;
        }

        db.close(); //Stänger databasen
        cursor.close();


        return workpasses;
    }

    public List<Company> getCompanysUnsynced(){
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "select * from " + CompanyEntry.TABLE_NAME
                + " where " + CompanyEntry.IS_SYNCED + "=?";

        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(0)});



        List<Company> companies = new ArrayList<>(5);
        while (cursor.moveToNext()){
            Company company = new Company(
                    cursor.getLong(cursor.getColumnIndex(CompanyEntry.COMPANY_ID)),
                    cursor.getInt(cursor.getColumnIndex(CompanyEntry.COMPANY_MY_SQL_ID)),
                    cursor.getInt(cursor.getColumnIndex(CompanyEntry.USER_ID)),
                    cursor.getString(cursor.getColumnIndex(CompanyEntry.COMPANY_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(CompanyEntry.WAGE)),
                    cursor.getInt(cursor.getColumnIndex(CompanyEntry.IS_SYNCED)),
                    cursor.getString(cursor.getColumnIndex(CompanyEntry.ACTION_TAG))
            );

            companies.add(company);
            db.close(); //Stänger databasen
        }

        return companies;
    }

    public List<Workpass> getWorkpassesUnsynced() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + WorkpassEntry.TABLE_NAME
                + " WHERE " + WorkpassEntry.IS_SYNCED + "=?", new String[]{String.valueOf(0)});



        List<Workpass> workpasses = new ArrayList<>();
        while (cursor.moveToNext()) {
            Workpass model = populateModelFromCursor(cursor);
            workpasses.add(model);
        }

        db.close(); //Stänger databasen

        return workpasses;
    }

    public boolean deleteWorkpass(Workpass workpass) {
        int result = this.getWritableDatabase().delete(WorkpassEntry.TABLE_NAME,
                WorkpassEntry.WORKPASS_ID + "=?", new String[]{String.valueOf(workpass.getWorkpassID())});

        return result > 0;
    }


    public boolean updateWorkpass(Workpass model) {
        ContentValues values = populateContentValuesFromModel(model);
        int result = this.getWritableDatabase().update(
                WorkpassEntry.TABLE_NAME,
                values,
                WorkpassEntry.WORKPASS_ID + "=?",
                new String[]{String.valueOf(model.getWorkpassID())});

        return result > 0;
    }

    private ContentValues populateContentValuesFromModel(Workpass model) {
        ContentValues values = new ContentValues();

        values.put(WorkpassEntry.WORKPASS_MY_SQL_ID, model.getServerID());

        values.put(WorkpassEntry.COMPANY_ID, model.getCompanyID());
        values.put(WorkpassEntry.COMPANY_MY_SQL_ID, model.getCompanyServerID());

        values.put(WorkpassEntry.USER_ID, model.getUserId());
        values.put(WorkpassEntry.TITLE, model.getTitle());

        String formattedStartDateTime = formatCalendarToString(model.getStartDateTime());
        values.put(WorkpassEntry.START_TIME, formattedStartDateTime);

        String formattedEndDateTime = formatCalendarToString(model.getEndDateTime());
        values.put(WorkpassEntry.END_TIME, formattedEndDateTime);

        values.put(WorkpassEntry.BREAK_TIME, model.getBreaktime());

        values.put(WorkpassEntry.SALARY, model.getSalary());

        values.put(WorkpassEntry.WORKED_HOURS, model.getWorkingHours());

        values.put(WorkpassEntry.NOTE, model.getNote());

        values.put(WorkpassEntry.IS_SYNCED, model.getIsSynced());

        values.put(WorkpassEntry.ACTION_TAG, model.getActionTag());

        values.put(WorkpassEntry.MONTH, model.getStartDateTime().get(Calendar.MONTH));

        return values;
    }


    private Workpass populateModelFromCursor(Cursor c) {
        Workpass model = new Workpass();

        model.setWorkpassID(c.getLong(c.getColumnIndex(WorkpassEntry.WORKPASS_ID)));

        model.setServerID(c.getInt(c.getColumnIndex(WorkpassEntry.WORKPASS_MY_SQL_ID)));

        model.setUserId(c.getColumnIndex(WorkpassEntry.USER_ID));

        model.setCompanyID(c.getLong(c.getColumnIndex(WorkpassEntry.COMPANY_ID)));
        model.setCompanyServerID(c.getInt(c.getColumnIndex(WorkpassEntry.COMPANY_MY_SQL_ID)));

        model.setTitle(c.getString(c.getColumnIndex(WorkpassEntry.TITLE)));

        GregorianCalendar startDateTime = formatStringToCalendar(c.getString(c.getColumnIndex(WorkpassEntry.START_TIME)));
        model.setStartDateTime(startDateTime);

        GregorianCalendar endDateTime = formatStringToCalendar(c.getString(c.getColumnIndex(WorkpassEntry.END_TIME)));
        model.setEndDateTime(endDateTime);

        model.setBreaktime(c.getInt(c.getColumnIndex(WorkpassEntry.BREAK_TIME)));

        model.setSalary(c.getDouble(c.getColumnIndex(WorkpassEntry.SALARY)));

        model.setNote(c.getString(c.getColumnIndex(WorkpassEntry.NOTE)));

        model.setWorkingHours(c.getInt(c.getColumnIndex(WorkpassEntry.WORKED_HOURS)));

        model.setIsSynced(c.getInt(c.getColumnIndex(WorkpassEntry.IS_SYNCED)));

        model.setActionTag(c.getString(c.getColumnIndex(WorkpassEntry.ACTION_TAG)));

        return model;
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

        TimeZone timeZone = TimeZone.getTimeZone("GMT+1");
        GregorianCalendar cal = new GregorianCalendar(timeZone);
        cal.setTime(date);

        return cal;
    }


}
