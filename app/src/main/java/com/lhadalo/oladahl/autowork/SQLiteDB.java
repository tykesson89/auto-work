package com.lhadalo.oladahl.autowork;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import UserPackage.User;

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
        // TODO Auto-generated method stub
        db.execSQL(
                "create table if not exists Users( " +
                        "id INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT," +
                        "userID INTEGER UNIQUE UNIQUE NOT NULL," +
                        "firstname TEXT NOT NULL, " +
                        "lastname TEXT NOT NULL, " +
                        "email TEXT NOT NULL," +
                        "salary REAL NOT NULL," +
                        "workplacename TEXT NOT NULL)" );
        Log.d("Table 1", "created");
        db.execSQL(
                "create table if not exists Workplace( " +
                        "id INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT," +
                        "workplaceID INTEGER UNIQUE NOT NULL, " +
                        "userID INTEGER NOT NULL, " +
                        "workplaceName TEXT NOT NULL, " +
                        "salary REAL NOT NULL)");
        Log.d("Table 2", "created");
        db.execSQL(
                "create table if not exists Workdays( " +
                        "id INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT," +
                        "workdayID INTEGER UNIQUE NOT NULL, " +
                        "userID INTEGER NOT NULL," +
                        "workplaceID INTEGER NOT NULL, " +
                        "workplaceName TEXT NOT NULL, " +
                        "date TEXT NOT NULL," +
                        "startTime TEXT NOT NULL," +
                        "endTime TEXT NOT NULL," +
                        "salary REAL NOT NULL)");
        Log.d("Table 3", "created");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users" + "DROP TABLE IF EXISTS Workplace" + "DROP TABLE IF EXISTS Workdays");
        onCreate(db);
    }


    public boolean createUser  (User user) {
        int userid = user.getUserid();
        String firstname = user.getFirstname();
        String lastname = user.getLastname();
        String email = user.getEmail();
        double salary = user.getHourlyWage();
        String workplace = user.getCompanyName();
        int workplaceid = user.getCompanyid();
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("Open Database", " ");
        ContentValues users = new ContentValues();
        users.put("userID", userid);
        users.put("firstname", firstname);
        users.put("lastname", lastname);
        users.put("email", email);
        users.put("salary", salary);
        users.put("workplacename", workplace);
        Long id = db.insert("Users", null, users);
        if(id == -1){
            Log.d("user Not inserted", " ");
        }


        ContentValues workPlace = new ContentValues();
        workPlace.put("workplaceID", workplaceid);
        workPlace.put("userID", userid);
        workPlace.put("workplacename", workplace);
        workPlace.put("salary", salary);
        Long id1 = db.insert("Workplace", null, workPlace);
        if(id1 == -1){
            Log.d("Not inserted", " ");
        }
        return true;
    }


    public boolean isLoggedIn(){
        String countQuery = "SELECT  * FROM " + "Users";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        if(count == 0){
            return false;
        }
        return true;
    }
    public User getUser() {

        final String TABLE_NAME = "Users";

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        User user = null;


            String firstname = cursor.getString(cursor.getColumnIndex("firstname"));
            String lastname = cursor.getString(cursor.getColumnIndex("lastname"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String companyname = cursor.getString(cursor.getColumnIndex("workplacename"));
            double salary = cursor.getDouble(cursor.getColumnIndex("salary"));
             user = new User(firstname, lastname, email, null, companyname, salary);

        cursor.close();
        return user;
    }


}
