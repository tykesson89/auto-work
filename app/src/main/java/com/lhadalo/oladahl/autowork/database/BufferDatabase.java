package com.lhadalo.oladahl.autowork.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Timestamp;
import java.util.Stack;

import UserPackage.BufferModel;

import com.lhadalo.oladahl.autowork.database.DatabaseContract.BufferEntry;

/**
 * Created by Henrik on 2016-04-05.
 */
public class BufferDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "AutoWork_Buffer_DB";

    public BufferDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLiteCommand.DB_CREATE_BUFFER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.BufferEntry.TABLE_NAME);

        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public Boolean isEmpty(){
        String countQuery = "SELECT  * FROM buffer";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        db.close();
        if(cnt == 0){
            return true;
        }else{
            return false;
        }

    }
    public Stack<BufferModel> getAllFromBuffer() {
        Cursor c = this.getReadableDatabase().rawQuery(
                "SELECT * FROM " + BufferEntry.TABLE_NAME, null);
        Stack<BufferModel> bufferModels = new Stack<>();

        while (c.moveToNext()) {
            BufferModel model = new BufferModel();
            model.setUserId(c.getColumnIndex(BufferEntry.COLUMN_USER_ID));
            model.setTitle(c.getString(c.getColumnIndex(BufferEntry.COLUMN_TITLE)));
            model.setStartDateTime(Timestamp.valueOf(c.getString(c.getColumnIndex(BufferEntry.COLUMN_START_DATE_TIME))));
            model.setEndDateTime(Timestamp.valueOf(c.getString(c.getColumnIndex(BufferEntry.COLUMN_END_DATE_TIME))));
            model.setBreaktime(c.getInt(c.getColumnIndex(BufferEntry.COLUMN_BRAKE_TIME)));
            model.setSalary(c.getDouble(c.getColumnIndex(BufferEntry.COLUMN_SALARY)));
            model.setNote(c.getString(c.getColumnIndex(BufferEntry.COLUMN_NOTE)));
            model.setWorkingHours(c.getInt(c.getColumnIndex(BufferEntry.COLUMN_HOURS)));
            model.setCompanyId(c.getInt(c.getColumnIndex(BufferEntry.COLUMN_COMPANY_ID)));
            model.setCompanyName(c.getString(c.getColumnIndex(BufferEntry.COLUMN_COMPANY_NAME)));
            model.setTag(c.getString(c.getColumnIndex(BufferEntry.COLUMN_TAG)));
            model.setHourlyWage(c.getDouble(c.getColumnIndex(BufferEntry.COLUMN_HOURLY_WAGE)));
            bufferModels.push(model);
        }

        return bufferModels;
    }
}
