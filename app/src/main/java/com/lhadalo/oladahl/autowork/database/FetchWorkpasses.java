package com.lhadalo.oladahl.autowork.database;

import android.content.Context;
import android.os.AsyncTask;

import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.activities.MainActivity;

import java.util.List;

import UserPackage.Workpass;

public class FetchWorkpasses extends AsyncTask<Integer, Void, List<Workpass>> {
    private MainActivity activity;
    int source;
    private SQLiteDB db;

    public static FetchWorkpasses newInstance(Context context, int source) {
        return new FetchWorkpasses(context, source);
    }

    private FetchWorkpasses(Context context, int source) {
        this.activity = (MainActivity) context;
        this.source = source;
        db = new SQLiteDB(context);
    }

    @Override
    protected List<Workpass> doInBackground(Integer... integers) {
        if (source == 3) {
            return db.getAllWorkpasses();
        } else {
            return db.getWorkpassMonth(integers[0]);
        }
    }

    @Override
    protected void onPostExecute(List<Workpass> workpasses) {
        if (source == Tag.ON_CREATE_LIST) {
            activity.onCreateList(workpasses);
        } else if (source == Tag.ON_UPDATE_LIST) {
            activity.updateList(workpasses);
        } else{
            activity.getStatistics(workpasses);
        }

    }
}