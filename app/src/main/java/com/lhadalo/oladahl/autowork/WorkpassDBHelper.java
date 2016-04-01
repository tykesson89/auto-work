package com.lhadalo.oladahl.autowork;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lhadalo.oladahl.autowork.WorkpassContract.WorkpassEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oladahl on 16-03-28.
 */
public class WorkpassDBHelper extends SQLiteOpenHelper{
    private static final int DB_VERSION = 1;
    private static final String DATABASE_NAME = "WorkpassDatabase.db";

    public WorkpassDBHelper(Context context){
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQLiteCommand.DB_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "
                + WorkpassContract.WorkpassEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long addWorkpass(WorkpassModel model){
        ContentValues values = new ContentValues();

        values.put(WorkpassEntry.COLUMN_TITLE, model.getTitle());
        values.put(WorkpassEntry.COLUMN_BRAKE_TIME, model.getBreaktime());
        values.put(WorkpassEntry.COLUMN_NOTE, model.getNote());

        return this.getWritableDatabase().insert(WorkpassEntry.TABLE_NAME, null, values);
    }

    public List<WorkpassModel> getAllWorkpasses(){
        Cursor cursor = this.getReadableDatabase().rawQuery(
                "SELECT * FROM " + WorkpassEntry.TABLE_NAME, null);
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
        model.setTitle(c.getString(c.getColumnIndex(WorkpassEntry.COLUMN_TITLE)));
        model.setBreaktime(c.getInt(c.getColumnIndex(WorkpassEntry.COLUMN_BRAKE_TIME)));
        model.setNote(c.getString(c.getColumnIndex(WorkpassEntry.COLUMN_NOTE)));

        return model;
    }
}
