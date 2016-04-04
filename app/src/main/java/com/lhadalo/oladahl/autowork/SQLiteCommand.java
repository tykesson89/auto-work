package com.lhadalo.oladahl.autowork;

import com.lhadalo.oladahl.autowork.WorkpassContract.WorkpassEntry;

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
                    + WorkpassEntry.COLUMN_WORKPLACE_ID + TEXT_TYPE + COMMA_SEP
                    + WorkpassEntry.COLUMN_START_DATE_TIME + TEXT_TYPE + COMMA_SEP
                    + WorkpassEntry.COLUMN_END_DATE_TIME + TEXT_TYPE + COMMA_SEP
                    + WorkpassEntry.COLUMN_SALARY + REAL_TYPE + COMMA_SEP
                    + WorkpassEntry.COLUMN_BRAKE_TIME + REAL_TYPE + COMMA_SEP
                    + WorkpassEntry.COLUMN_HOURS + REAL_TYPE + COMMA_SEP
                    + WorkpassEntry.COLUMN_NOTE + TEXT_TYPE + ");";


}
