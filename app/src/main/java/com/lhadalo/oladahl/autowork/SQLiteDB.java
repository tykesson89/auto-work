package com.lhadalo.oladahl.autowork;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Henrik on 2016-02-29.
 */
public class SQLiteDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AutoWork_DB";

    public SQLiteDB(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table if not exists Users( " +
                        "userID INTEGER UNIQUE PRIMARY KEY UNIQUE NOT NULL," +
                        "firstname TEXT NOT NULL," +
                        "lastname TEXT NOT NULL" +
                        "email TEXT NOT NULL," +
                        "salary REAL NOT NULL," +
                        "workplace TEXT NOT NULL)" +


                        "create table if not exists Workplace( " +
                        "workplaceID INTEGER PRIMARY KEY UNIQUE NOT NULL" +
                        "userID INTEGER NOT NULL, " +
                        "workplaceName TEXT NOT NULL, " +
                        "salary REAL NOT NULL)" +


                        "create table if not exists Workdays( " +
                        "workdayID INTEGER PRIMARY KEY UNIQUE NOT NULL," +
                        "userID INTEGER NOT NULL," +
                        "workplaceID INTEGER NOT NULL," +
                        "workplaceName TEXT NOT NULL " +
                        "date TEXT NOT NULL," +
                        "startTime TEXT NOT NULL," +
                        "endTime TEXT NOT NULL," +
                        "salary REAL NOT NULL)"

        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users" + "DROP TABLE IF EXISTS Workplace" + "DROP TABLE IF EXISTS Workdays");
        onCreate(db);
    }


    public boolean createUser  (String userID, String firstname, String lastname, String email, String sSalary, String workplace, String workplaceID) {
        int userid = Integer.parseInt(userID);
        int workplaceid = Integer.parseInt(workplaceID);
        double salary = Double.parseDouble(sSalary);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues user = new ContentValues();
        user.put("userID", userid);
        user.put("firstname", firstname);
        user.put("lastname", lastname);
        user.put("email", email);
        user.put("salary", salary);
        user.put("workplace", workplace);
        db.insert("Users", null, user);
        ContentValues workPlace = new ContentValues();
        workPlace.put("workplaceID", workplaceid);
        workPlace.put("userID", userid);
        workPlace.put("workplace", workplace);
        db.insert("Workplace", null, workPlace);
        return true;
    }


    public boolean isLoggedIn(){
        String countQuery = "SELECT  * FROM " + "Users";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        int count = cursor.getCount();
        if(count == 0){
            return false;
        }
        return true;
    }


}
