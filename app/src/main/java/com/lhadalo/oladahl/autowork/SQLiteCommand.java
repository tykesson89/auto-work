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
    private static final String NOT_NULL = " NOT NULL";

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
                    + WorkpassEntry.COLUMN_NOTE + TEXT_TYPE
                    + ");";

    public static final String DB_CREATE_USER_TABLE =
            "create table if not exists " + UserEntry.TABLE_NAME + " ("
                    + UserEntry.USER_ID + " INTEGER UNIQUE PRIMARY KEY" + COMMA_SEP
                    + UserEntry.EMAIL + TEXT_TYPE + NOT_NULL + COMMA_SEP
                    + UserEntry.FIRST_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP
                    + UserEntry.LAST_NAME + TEXT_TYPE + NOT_NULL
                    + ");";


    public static final String DB_CREATE_COMPANY_TABLE =
            "create table if not exists " + CompanyEntry.TABLE_NAME + " ("
                    + CompanyEntry.COMPANY_ID + " INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT" + COMMA_SEP
                    + CompanyEntry.COMPANY_MY_SQL_ID + INT_TYPE + COMMA_SEP
                    + CompanyEntry.USER_ID + INT_TYPE + NOT_NULL + COMMA_SEP
                    + CompanyEntry.COMPANY_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP
                    + CompanyEntry.WAGE + REAL_TYPE + NOT_NULL + COMMA_SEP
                    + CompanyEntry.IS_SYNCED + INT_TYPE  + COMMA_SEP
                    + CompanyEntry.ACTION_TAG + TEXT_TYPE
                    + ");";

    public static final String DB_CREATE_BUFFER_TABLE =
            "CREATE TABLE if not exists " + BufferEntry.TABLE_NAME + " ("
                    + BufferEntry.WORKPASS_ID + " INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT" + COMMA_SEP
                    + BufferEntry.COLUMN_USER_ID + INT_TYPE + COMMA_SEP
                    + BufferEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP
                    + BufferEntry.COLUMN_WORKPLACE_ID + TEXT_TYPE + COMMA_SEP
                    + BufferEntry.COLUMN_START_DATE_TIME + TEXT_TYPE + COMMA_SEP
                    + BufferEntry.COLUMN_END_DATE_TIME + TEXT_TYPE + COMMA_SEP
                    + BufferEntry.COLUMN_SALARY + REAL_TYPE + COMMA_SEP
                    + BufferEntry.COLUMN_BRAKE_TIME + REAL_TYPE + COMMA_SEP
                    + BufferEntry.COLUMN_HOURS + REAL_TYPE + COMMA_SEP
                    + BufferEntry.COLUMN_COMPANY_ID + INT_TYPE + COMMA_SEP
                    + BufferEntry.COLUMN_COMPANY_NAME + TEXT_TYPE + COMMA_SEP
                    + BufferEntry.COLUMN_HOURLY_WAGE + REAL_TYPE + COMMA_SEP
                    + BufferEntry.COLUMN_TAG + TEXT_TYPE + COMMA_SEP
                    + BufferEntry.COLUMN_NOTE + TEXT_TYPE + ");";

}
