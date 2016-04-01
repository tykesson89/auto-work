package com.lhadalo.oladahl.autowork;
import com.lhadalo.oladahl.autowork.WorkpassContract.WorkpassEntry;
/**
 * Created by oladahl on 16-03-26.
 */
public class SQLiteCommand {
    private static final String COMMA_SEP = ", ";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";

    public static final String DB_CREATE_TABLE =
            "CREATE TABLE " + WorkpassEntry.TABLE_NAME + " ("
            + WorkpassEntry._ID + " INTEGER PRIMARY KEY " + COMMA_SEP
            + WorkpassEntry.COLUMN_USER_ID + INT_TYPE + COMMA_SEP
            + WorkpassEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP
            + WorkpassEntry.COLUMN_WORKPLACE_ID + TEXT_TYPE + COMMA_SEP
            + WorkpassEntry.COLUMN_START_DATE_TIME + TEXT_TYPE + COMMA_SEP
            + WorkpassEntry.COLUMN_END_DATE_TIME + TEXT_TYPE + COMMA_SEP
            + WorkpassEntry.COLUMN_BRAKE_TIME + INT_TYPE + COMMA_SEP
            + WorkpassEntry.COLUMN_NOTE + TEXT_TYPE + ");";


}
