package com.lhadalo.oladahl.autowork;

import com.lhadalo.oladahl.autowork.WorkpassContract.WorkpassEntry;
import com.lhadalo.oladahl.autowork.WorkpassContract.BufferEntry;

import com.lhadalo.oladahl.autowork.DatabaseContract.UserEntry;
import com.lhadalo.oladahl.autowork.DatabaseContract.CompanyEntry;

/**
 * Created by oladahl on 16-03-26.
 */
public class SQLiteCommand {
    private static final String COMMA_SEP = ", ";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";

    public static final String DB_CREATE_WORKPASS_TABLE =
            "CREATE TABLE if not exists " + WorkpassEntry.TABLE_NAME + " ("
                    + WorkpassEntry.WORKPASS_ID + " INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT" + COMMA_SEP
                    + WorkpassEntry.COLUMN_USER_ID + INT_TYPE + COMMA_SEP
                    + WorkpassEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP
                    + WorkpassEntry.COLUMN_COMPANY_ID + TEXT_TYPE + COMMA_SEP
                    + WorkpassEntry.COLUMN_START_DATE_TIME + TEXT_TYPE + COMMA_SEP
                    + WorkpassEntry.COLUMN_END_DATE_TIME + TEXT_TYPE + COMMA_SEP
                    + WorkpassEntry.COLUMN_SALARY + REAL_TYPE + COMMA_SEP
                    + WorkpassEntry.COLUMN_BRAKE_TIME + REAL_TYPE + COMMA_SEP
                    + WorkpassEntry.COLUMN_HOURS + REAL_TYPE + COMMA_SEP
                    + WorkpassEntry.COLUMN_NOTE + TEXT_TYPE + ");";

    public static final String DB_CREATE_USER_TABLE =
            "create table if not exists " + UserEntry.USER_ID


}
