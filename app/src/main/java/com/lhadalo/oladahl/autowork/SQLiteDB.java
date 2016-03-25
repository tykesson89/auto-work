package com.lhadalo.oladahl.autowork;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
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
                        "email TEXT NOT NULL)" );
        Log.d("Table 1", "created");
        db.execSQL(
                "create table if not exists Company( " +
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
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Users",null,null);
        db.delete("Company",null,null);
        db.delete("Workdays",null,null);
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

    public int getUserId(Context context){
        SQLiteDatabase db = this.getReadableDatabase();
        int s;
        Cursor c = db.rawQuery("SELECT * FROM Users", null);
        c.moveToFirst();
        s=c.getInt(c.getColumnIndex("UserID"));
        db.close();
        return s;
    }

    public String getFirstName(){
        SQLiteDatabase db = this.getReadableDatabase();
        String string;
        Cursor c = db.rawQuery("SELECT * FROM Users", null);
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

    


}
