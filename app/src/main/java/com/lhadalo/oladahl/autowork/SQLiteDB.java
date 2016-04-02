package com.lhadalo.oladahl.autowork;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import UserPackage.Company;
import UserPackage.User;
import UserPackage.WorkpassModel;

import com.lhadalo.oladahl.autowork.WorkpassContract.WorkpassEntry;

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
        db.execSQL(
                "create table if not exists Users( " +
                        "id INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT," +
                        "userID INTEGER UNIQUE UNIQUE NOT NULL," +
                        "firstname TEXT NOT NULL, " +
                        "lastname TEXT NOT NULL, " +
                        "email TEXT NOT NULL)");
        Log.d("Table 1", "created");
        db.execSQL(
                "create table if not exists Company( " +
                        "id INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT," +
                        "companyId INTEGER UNIQUE NOT NULL, " +
                        "userID INTEGER NOT NULL, " +
                        "companyName TEXT NOT NULL, " +
                        "Hourlywage REAL NOT NULL)");
        Log.d("Table 2", "created");

        db.execSQL(SQLiteCommand.DB_CREATE_TABLE);
        Log.d("Table 3", "created");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users" + "DROP TABLE IF EXISTS Company" + "DROP TABLE IF EXISTS workpass");
        onCreate(db);
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Users",null,null);
        db.delete("Company",null,null);
        db.delete("workpass",null,null);
        Log.d("Database: ", "Deleted");
        onCreate(db);
        db.close();
    }

    public boolean loginUser(User user){
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
        return true;
    }
    public void addCompany(Company company){
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

    }
    public void addloginWorkpass(WorkpassModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WorkpassEntry.COLUMN_USER_ID, model.getUserId());
        values.put(WorkpassEntry.COLUMN_TITLE, model.getTitle());
        values.put(WorkpassEntry.COLUMN_WORKPLACE_ID, model.getId());
        values.put(WorkpassEntry.COLUMN_START_DATE_TIME, model.getStartDateTime().toString());
        values.put(WorkpassEntry.COLUMN_END_DATE_TIME, model.getEndDateTime().toString());
        values.put(WorkpassEntry.COLUMN_SALARY, model.getSalary());
        values.put(WorkpassEntry.COLUMN_BRAKE_TIME, model.getBreaktime());
        values.put(WorkpassEntry.COLUMN_NOTE, model.getNote());

        db.insert("workpass", null, values);

    }



    public int getUserId(Context context){
        SQLiteDatabase db = this.getReadableDatabase();
        int s;
        Cursor c = db.rawQuery("SELECT userID FROM Users", null);
        c.moveToFirst();
        s=c.getInt(c.getColumnIndex("userID"));
        db.close();
        return s;
    }

    public String getFirstName(){
        SQLiteDatabase db = this.getReadableDatabase();
        String string;
        Cursor c = db.rawQuery("SELECT firstname FROM Users", null);
        c.moveToFirst();
        string=c.getString(c.getColumnIndex("firstname"));
        db.close();
        return string;
    }

    public Boolean isLoggedIn(){
        String countQuery = "SELECT  * FROM Users";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        if(cnt == 0){
            return false;
        }else{
            return true;
        }
    }

    //Workpass----------------------------------------------------------------------------
    public long addWorkpass(WorkpassModel model){
        ContentValues values = new ContentValues();

        values.put(WorkpassEntry.COLUMN_USER_ID, model.getUserId());
        values.put(WorkpassEntry.COLUMN_TITLE, model.getTitle());
        values.put(WorkpassEntry.COLUMN_WORKPLACE_ID, model.getId());
        values.put(WorkpassEntry.COLUMN_START_DATE_TIME, model.getStartDateTime().toString());
        values.put(WorkpassEntry.COLUMN_END_DATE_TIME, model.getEndDateTime().toString());
        values.put(WorkpassEntry.COLUMN_SALARY, model.getSalary());
        values.put(WorkpassEntry.COLUMN_BRAKE_TIME, model.getBreaktime());
        values.put(WorkpassEntry.COLUMN_NOTE, model.getNote());

        return this.getWritableDatabase().insert(WorkpassContract.WorkpassEntry.TABLE_NAME, null, values);
    }

    public List<WorkpassModel> getAllWorkpasses(){
        Cursor cursor = this.getReadableDatabase().rawQuery(
                "SELECT * FROM " + WorkpassContract.WorkpassEntry.TABLE_NAME, null);
        List<WorkpassModel> workpassModels = new ArrayList<>();

        while(cursor.moveToNext()){
            WorkpassModel model = populateModel(cursor);
            workpassModels.add(model);
        }

        return workpassModels;
    }

    private WorkpassModel populateModel(Cursor c){
        WorkpassModel model = new WorkpassModel();

        // model.setId(c.getLong(c.getColumnIndex(WorkpassEntry._ID)));
        model.setTitle(c.getString(c.getColumnIndex(WorkpassContract.WorkpassEntry.COLUMN_TITLE)));
        model.setBreaktime(c.getInt(c.getColumnIndex(WorkpassContract.WorkpassEntry.COLUMN_BRAKE_TIME)));
        model.setNote(c.getString(c.getColumnIndex(WorkpassContract.WorkpassEntry.COLUMN_NOTE)));

        return model;
    }
}
