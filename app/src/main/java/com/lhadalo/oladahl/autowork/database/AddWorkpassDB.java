package com.lhadalo.oladahl.autowork.database;

import android.content.Context;

import UserPackage.Workpass;

/**
 * Created by oladahl on 16-04-30.
 */
public class AddWorkpassDB extends Thread{
    SQLiteDB database;
    private Workpass workpass;

    public static AddWorkpassDB addWorkpass(Context context, Workpass workpass){
        return new AddWorkpassDB(context, workpass);
    }

    private AddWorkpassDB(Context context, Workpass workpass){
        database = new SQLiteDB(context);
        this.workpass = workpass;
        start();
    }

    @Override
    public void run() {
        database.addWorkpass(workpass);

        interrupt();
    }
}