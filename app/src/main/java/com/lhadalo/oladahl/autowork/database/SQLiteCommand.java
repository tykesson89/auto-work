package com.lhadalo.oladahl.autowork.database;



import com.lhadalo.oladahl.autowork.database.DatabaseContract.UserEntry;
import com.lhadalo.oladahl.autowork.database.DatabaseContract.CompanyEntry;
import com.lhadalo.oladahl.autowork.database.DatabaseContract.WorkpassEntry;

public class SQLiteCommand {
    private static final String COMMA_SEP = ", ";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String NOT_NULL = " NOT NULL";
    private static final String PRIMARY_KEY = " INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT";

    public static final String DB_CREATE_WORKPASS_TABLE =
            "create table if not exists " + WorkpassEntry.TABLE_NAME + " ("
                    + WorkpassEntry.WORKPASS_ID + PRIMARY_KEY + COMMA_SEP
                    + WorkpassEntry.WORKPASS_MY_SQL_ID + INT_TYPE + COMMA_SEP
                    + WorkpassEntry.COMPANY_ID + INT_TYPE + COMMA_SEP
                    + WorkpassEntry.COMPANY_MY_SQL_ID + INT_TYPE + COMMA_SEP
                    + WorkpassEntry.USER_ID + INT_TYPE + COMMA_SEP
                    + WorkpassEntry.TITLE + TEXT_TYPE + COMMA_SEP
                    + WorkpassEntry.START_TIME + TEXT_TYPE + COMMA_SEP
                    + WorkpassEntry.END_TIME + TEXT_TYPE + COMMA_SEP
                    + WorkpassEntry.BREAK_TIME + REAL_TYPE + COMMA_SEP
                    + WorkpassEntry.WORKED_HOURS + REAL_TYPE + COMMA_SEP
                    + WorkpassEntry.SALARY + REAL_TYPE + COMMA_SEP
                    + WorkpassEntry.NOTE + TEXT_TYPE + COMMA_SEP
                    + WorkpassEntry.IS_SYNCED + INT_TYPE + COMMA_SEP
                    + WorkpassEntry.ACTION_TAG + TEXT_TYPE + COMMA_SEP
                    + WorkpassEntry.MONTH + INT_TYPE
                    + ");";

    public static final String DB_CREATE_USER_TABLE =
            "create table if not exists " + UserEntry.TABLE_NAME + " ("
                    + UserEntry.USER_ID + PRIMARY_KEY + COMMA_SEP
                    + UserEntry.EMAIL + TEXT_TYPE + NOT_NULL + COMMA_SEP
                    + UserEntry.FIRST_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP
                    + UserEntry.LAST_NAME + TEXT_TYPE + NOT_NULL
                    + ");";


    public static final String DB_CREATE_COMPANY_TABLE =
            "create table if not exists " + CompanyEntry.TABLE_NAME + " ("
                    + CompanyEntry.COMPANY_ID + PRIMARY_KEY + COMMA_SEP
                    + CompanyEntry.COMPANY_MY_SQL_ID + INT_TYPE + COMMA_SEP
                    + CompanyEntry.USER_ID + INT_TYPE + NOT_NULL + COMMA_SEP
                    + CompanyEntry.COMPANY_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP
                    + CompanyEntry.WAGE + REAL_TYPE + NOT_NULL + COMMA_SEP
                    + CompanyEntry.IS_SYNCED + INT_TYPE  + COMMA_SEP
                    + CompanyEntry.ACTION_TAG + TEXT_TYPE
                    + ");";



}
