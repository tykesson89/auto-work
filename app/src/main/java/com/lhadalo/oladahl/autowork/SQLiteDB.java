package com.lhadalo.oladahl.autowork;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


import UserPackage.Company;
import UserPackage.User;
import UserPackage.WorkpassModel;

import com.lhadalo.oladahl.autowork.WorkpassContract.WorkpassEntry;

/**
 * Created by Henrik on 2016-02-29.
 */
public class SQLiteDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "AutoWork_DB";

    public SQLiteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table if not exists Users( " +
                        "id INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT," +
                        "userID INTEGER NOT NULL," +
                        "firstname TEXT NOT NULL, " +
                        "lastname TEXT NOT NULL, " +
                        "email TEXT NOT NULL)");
        Log.d("Table 1", "created");
        db.execSQL(
                "create table if not exists Company( " +
                        "id INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT," +
                        "companyId INTEGER, " +
                        "userID INTEGER, " +
                        "companyName TEXT UNIQUE NOT NULL, " +
                        "Hourlywage REAL NOT NULL)");
        Log.d("Table 2", "created");

        db.execSQL(SQLiteCommand.DB_CREATE_WORKPASS_TABLE);
        Log.d("Table 3", "created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Workplace");
        db.execSQL("DROP TABLE IF EXISTS " + WorkpassEntry.TABLE_NAME);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Users", null, null);
        db.delete("Company", null, null);
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
        data.put("firstname", firstname);
        data.put("lastname", lastname);
        data.put("email", email);
        db.update("users", data, "userid=" + id, null);
        db.close();


    }

    public boolean loginUser(User user) {
        int userid = user.getUserid();
        String firstname = user.getFirstname();
        String lastname = user.getLastname();
        String email = user.getEmail();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("userID", userid);
        content.put("firstname", firstname);
        content.put("lastname", lastname);
        content.put("email", email);

        db.insert("Users", null, content);
        db.close();
        return true;
    }

    //Company---------------------------------------------------------------------------------
    public void addCompany(Company company) {
        int companyId = company.getCompanyId();
        String companyName = company.getCompanyName();
        double hourtlyWage = company.getHourlyWage();
        int userid = company.getUserId();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("userID", userid);
        content.put("companyId", companyId);
        content.put("Hourlywage", hourtlyWage);
        content.put("companyName", companyName);

        db.insert("Company", null, content);

        db.close();



    }
    public double getHourlyWage(String companyName){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select hourlyWage from Company where companyName = ?",
                new String[]{companyName});

        cursor.moveToFirst();
       double hourlyWage= cursor.getDouble(0);
       return hourlyWage;

    }


    public void addCompanyLocal(Company company) {
        String companyName = company.getCompanyName();
        double hourtlyWage = company.getHourlyWage();
        int userid = company.getUserId();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("userID", userid);
        content.put("Hourlywage", hourtlyWage);
        content.put("companyName", companyName);

        db.insert("Company", null, content);

        db.close();
    }

    public List<Company> getAllCompanies() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM Company", null);
        List<Company> companies = new ArrayList<>();

        while (cursor.moveToNext()) {
            Company company = new Company(
                    cursor.getString(cursor.getColumnIndex("companyName")),
                    cursor.getDouble(cursor.getColumnIndex("Hourlywage")),
                    cursor.getInt(cursor.getColumnIndex("userID")),
                    cursor.getInt(cursor.getColumnIndex("companyId"))
            );

            companies.add(company);
        }

        return companies;
    }


    public Company getCompany(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from Company where companyId = ?",
                new String[]{String.valueOf(id)});

        cursor.moveToFirst();
        Company company = new Company(
                cursor.getString(cursor.getColumnIndex("companyName")),
                cursor.getDouble(cursor.getColumnIndex("Hourlywage")),
                cursor.getInt(cursor.getColumnIndex("userID")),
                cursor.getInt(cursor.getColumnIndex("companyId"))
        );

        db.close();
        return company;
    }


    //Company---------------------------------------------------------------------------------

    public void addloginWorkpass(WorkpassModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(WorkpassEntry.COLUMN_USER_ID, model.getUserId());
        values.put(WorkpassEntry.COLUMN_TITLE, model.getTitle());
        values.put(WorkpassEntry.COLUMN_COMPANY_ID, model.getId());
        values.put(WorkpassEntry.COLUMN_START_DATE_TIME, model.getStartDateTime().toString());
        values.put(WorkpassEntry.COLUMN_END_DATE_TIME, model.getEndDateTime().toString());
        values.put(WorkpassEntry.COLUMN_SALARY, model.getSalary());
        values.put(WorkpassEntry.COLUMN_BRAKE_TIME, model.getBreaktime());
        values.put(WorkpassEntry.COLUMN_NOTE, model.getNote());

        db.insert("workpass", null, values);
        db.close();
    }

    public User getUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        User user;
        Cursor c = db.rawQuery("SELECT * FROM Users", null);
        c.moveToFirst();
        int userID = c.getInt(c.getColumnIndex("userID"));
        String firstName = c.getString(c.getColumnIndex("firstname"));
        String lastName = c.getString(c.getColumnIndex("lastname"));
        String email = c.getString(c.getColumnIndex("email"));
        user = new User(firstName, lastName, email, userID);
        db.close();
        return user;
    }

    public int getUserId(Context context) {
        SQLiteDatabase db = this.getReadableDatabase();
        int s;
        Cursor c = db.rawQuery("SELECT userID FROM Users", null);
        c.moveToFirst();
        s = c.getInt(c.getColumnIndex("userID"));
        db.close();
        return s;
    }

    public String getFirstName() {
        SQLiteDatabase db = this.getReadableDatabase();
        String string;
        Cursor c = db.rawQuery("SELECT firstname FROM Users", null);
        c.moveToFirst();
        string = c.getString(c.getColumnIndex("firstname"));
        db.close();
        return string;
    }


    public Boolean isLoggedIn() {
        String countQuery = "SELECT  * FROM Users";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        db.close();
        if (cnt == 0) {
            return false;
        } else {
            return true;
        }

    }


    public void changeCompany(String companyName, double hourly){
        SQLiteDatabase db = this.getWritableDatabase();


        String strSQL = "UPDATE Company SET HourlyWage= " + hourly + " WHERE companyName= '"+ companyName+"'";

        db.execSQL(strSQL);
        db.close();


    }


    public void deleteCompany(String companyName) {
        {
            SQLiteDatabase db = this.getWritableDatabase();
            try {
                db.delete("Company", "companyName = ?", new String[]{companyName});
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }

    }


    //Workpass-----------------------------------------------------------------------------------
    public long addWorkpass(WorkpassModel model) {
        ContentValues values = populateContentValuesFromModel(model);

        return this.getWritableDatabase().insert(WorkpassEntry.TABLE_NAME, null, values);
    }

    public List<WorkpassModel> getAllWorkpasses() {
        Cursor cursor = this.getReadableDatabase().rawQuery(
                "SELECT * FROM " + WorkpassEntry.TABLE_NAME, null);
        List<WorkpassModel> workpassModels = new ArrayList<>();

        while (cursor.moveToNext()) {
            WorkpassModel model = populateModelFromCursor(cursor);
            workpassModels.add(model);
        }

        return workpassModels;
    }

    public WorkpassModel getWorkpass(long id){
        String command = "SELECT * FROM " + WorkpassEntry.TABLE_NAME + " WHERE "
                + WorkpassEntry.WORKPASS_ID + "=" + String.valueOf(id);

        Cursor c = this.getReadableDatabase().rawQuery(command, null);

        c.moveToFirst();
        return this.populateModelFromCursor(c);
    }

    public WorkpassModel getLastAddedWorkpass(){
        String command = "SELECT * FROM " + WorkpassEntry.TABLE_NAME + " WHERE " + WorkpassEntry.WORKPASS_ID
                + " = (SELECT MAX(" + WorkpassEntry.WORKPASS_ID + ") FROM " + WorkpassEntry.TABLE_NAME + ");";


        Cursor c = this.getReadableDatabase().rawQuery(command, null);

        c.moveToFirst();
        return this.populateModelFromCursor(c);
    }

    public boolean deleteWorkpass(long id){
        int result = this.getWritableDatabase().delete(WorkpassEntry.TABLE_NAME,
                WorkpassEntry.WORKPASS_ID + "=?", new String[]{String.valueOf(id)});

        return result > 0;
    }

    public boolean updateWorkpass(WorkpassModel model){
        ContentValues values = populateContentValuesFromModel(model);
        int result = this.getWritableDatabase().update(
                WorkpassEntry.TABLE_NAME,
                values,
                WorkpassEntry.WORKPASS_ID + "=?",
                new String[]{String.valueOf(model.getId())});

        return result > 0;
    }

    private ContentValues populateContentValuesFromModel(WorkpassModel model){
        ContentValues values = new ContentValues();

        values.put(WorkpassEntry.COLUMN_USER_ID, model.getUserId());

        values.put(WorkpassEntry.COLUMN_COMPANY_ID, model.getCompany().getCompanyId());

        values.put(WorkpassEntry.COLUMN_TITLE, model.getTitle());

        String formattedStartDateTime = formatCalendarToString(model.getStartDateTime());
        values.put(WorkpassEntry.COLUMN_START_DATE_TIME, formattedStartDateTime);

        String formattedEndDateTime = formatCalendarToString(model.getEndDateTime());
        values.put(WorkpassEntry.COLUMN_END_DATE_TIME, formattedEndDateTime);

        values.put(WorkpassEntry.COLUMN_BRAKE_TIME, model.getBreaktime());

        values.put(WorkpassEntry.COLUMN_SALARY, model.getSalary());

        values.put(WorkpassEntry.COLUMN_HOURS, model.getWorkingHours());

        values.put(WorkpassEntry.COLUMN_NOTE, model.getNote());

        return values;
    }


    private WorkpassModel populateModelFromCursor(Cursor c) {
        WorkpassModel model = new WorkpassModel();

        model.setId(c.getLong(c.getColumnIndex(WorkpassEntry.WORKPASS_ID)));

        model.setUserId(c.getColumnIndex(WorkpassEntry.COLUMN_USER_ID));

        Company company = getCompany(c.getInt(c.getColumnIndex(WorkpassEntry.COLUMN_COMPANY_ID)));
        model.setCompany(company);

        model.setTitle(c.getString(c.getColumnIndex(WorkpassEntry.COLUMN_TITLE)));

        GregorianCalendar startDateTime = formatStringToCalendar(c.getString(c.getColumnIndex(WorkpassEntry.COLUMN_START_DATE_TIME)));
        model.setStartDateTime(startDateTime);

        GregorianCalendar endDateTime = formatStringToCalendar(c.getString(c.getColumnIndex(WorkpassEntry.COLUMN_END_DATE_TIME)));
        model.setEndDateTime(endDateTime);

        model.setBreaktime(c.getInt(c.getColumnIndex(WorkpassEntry.COLUMN_BRAKE_TIME)));

        model.setSalary(c.getDouble(c.getColumnIndex(WorkpassEntry.COLUMN_SALARY)));

        model.setNote(c.getString(c.getColumnIndex(WorkpassEntry.COLUMN_NOTE)));

        model.setWorkingHours(c.getInt(c.getColumnIndex(WorkpassEntry.COLUMN_HOURS)));

        return model;
    }

    public ArrayList<WorkpassModel> getSalaryAndDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<WorkpassModel> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT salary,enddatetime FROM Workpass", null);
        WorkpassModel workpass;
        while (c.moveToNext()) {
            workpass = new WorkpassModel();

            GregorianCalendar endDateTime = formatStringToCalendar(c.getString(c.getColumnIndex(WorkpassEntry.COLUMN_END_DATE_TIME)));
            workpass.setEndDateTime(endDateTime);
            workpass.setSalary(c.getDouble(c.getColumnIndex(WorkpassEntry.COLUMN_SALARY)));
            list.add(workpass);

        }
        db.close();

        return list;
    }

    public ArrayList<WorkpassModel> getHours() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<WorkpassModel> wpm = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT hours,enddatetime FROM Workpass", null);

        WorkpassModel workpass;
        while (c.moveToNext()) {
            workpass = new WorkpassModel();

            GregorianCalendar endDateTime = formatStringToCalendar(c.getString(c.getColumnIndex(WorkpassEntry.COLUMN_END_DATE_TIME)));
            workpass.setEndDateTime(endDateTime);
            workpass.setWorkingHours(c.getInt(c.getColumnIndex(WorkpassEntry.COLUMN_HOURS)));
            wpm.add(workpass);
        }
        db.close();
        return wpm;
    }

    public ArrayList<WorkpassModel> getNextPassHour() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<WorkpassModel> wpm = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT hours,enddatetime FROM Workpass", null);

        WorkpassModel workpass;
        while (c.moveToNext()) {
            workpass = new WorkpassModel();

            GregorianCalendar endDateTime = formatStringToCalendar(c.getString(c.getColumnIndex(WorkpassEntry.COLUMN_END_DATE_TIME)));
            workpass.setEndDateTime(endDateTime);
            workpass.setWorkingHours(c.getInt(c.getColumnIndex(WorkpassEntry.COLUMN_HOURS)));
            wpm.add(workpass);
        }
        db.close();
        return wpm;
    }

    public ArrayList<WorkpassModel> getNextPassSalary() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<WorkpassModel> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT salary,enddatetime FROM Workpass", null);
        WorkpassModel workpass;
        while (c.moveToNext()) {
            workpass = new WorkpassModel();

            GregorianCalendar endDateTime = formatStringToCalendar(c.getString(c.getColumnIndex(WorkpassEntry.COLUMN_END_DATE_TIME)));
            workpass.setEndDateTime(endDateTime);
            workpass.setSalary(c.getDouble(c.getColumnIndex(WorkpassEntry.COLUMN_SALARY)));
            list.add(workpass);

        }
        db.close();

        return list;
    }

    public ArrayList<WorkpassModel> showDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<WorkpassModel> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT startdatetime FROM Workpass", null);
        WorkpassModel workpass;
        while (c.moveToNext()) {
            workpass = new WorkpassModel();

            GregorianCalendar startDateTime = formatStringToCalendar(c.getString(c.getColumnIndex(WorkpassEntry.COLUMN_START_DATE_TIME)));
            workpass.setStartDateTime(startDateTime);

            list.add(workpass);

        }
        db.close();

        return list;
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
