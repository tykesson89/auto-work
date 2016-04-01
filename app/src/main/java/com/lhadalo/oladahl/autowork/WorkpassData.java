package com.lhadalo.oladahl.autowork;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lhadalo.oladahl.autowork.SQLiteDB;
import com.lhadalo.oladahl.autowork.WorkpassContract;
import com.lhadalo.oladahl.autowork.WorkpassContract.WorkpassEntry;
import com.lhadalo.oladahl.autowork.WorkpassModel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by oladahl on 16-04-01.
 */
public class WorkpassData {
    SQLiteDB dbHelper;
    SQLiteDatabase database;

    public WorkpassData(Context context) {
        dbHelper = new SQLiteDB(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addWorkpass(WorkpassModel model) {
        ContentValues values = new ContentValues();

        values.put(WorkpassEntry.COLUMN_USER_ID, model.getUserId());
        values.put(WorkpassEntry.COLUMN_WORKPLACE_ID, 1);
        values.put(WorkpassEntry.COLUMN_TITLE, model.getTitle());
        values.put(WorkpassEntry.COLUMN_START_DATE_TIME, model.getStartDateTime().toString());
        values.put(WorkpassEntry.COLUMN_END_DATE_TIME, model.getEndDateTime().toString());
        values.put(WorkpassEntry.COLUMN_BRAKE_TIME, model.getBreaktime());
        values.put(WorkpassEntry.COLUMN_SALARY, model.getSalary());
        values.put(WorkpassEntry.COLUMN_NOTE, model.getNote());

        return database.insert(WorkpassEntry.TABLE_NAME, null, values);
    }

    public List<WorkpassModel> getAllWorkpasses() {
        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + WorkpassEntry.TABLE_NAME, null);
        List<WorkpassModel> workpassModels = new ArrayList<>();

        while (cursor.moveToNext()) {
            WorkpassModel model = populateModel(cursor);
            workpassModels.add(model);
        }

        return workpassModels;
    }

    private WorkpassModel populateModel(Cursor c) {
        WorkpassModel model = new WorkpassModel();

        model.setUserId(c.getColumnIndex(WorkpassEntry.COLUMN_USER_ID));
        model.setCompany(null);
        model.setTitle(c.getString(c.getColumnIndex(WorkpassEntry.COLUMN_TITLE)));
        model.setStartDateTime(Timestamp.valueOf(c.getString(c.getColumnIndex(WorkpassEntry.COLUMN_START_DATE_TIME))));
        model.setEndDateTime(Timestamp.valueOf(c.getString(c.getColumnIndex(WorkpassEntry.COLUMN_END_DATE_TIME))));
        model.setBreaktime(c.getInt(c.getColumnIndex(WorkpassEntry.COLUMN_BRAKE_TIME)));
        model.setSalary(c.getDouble(c.getColumnIndex(WorkpassEntry.COLUMN_SALARY)));
        model.setNote(c.getString(c.getColumnIndex(WorkpassEntry.COLUMN_NOTE)));

        return model;
    }


}
