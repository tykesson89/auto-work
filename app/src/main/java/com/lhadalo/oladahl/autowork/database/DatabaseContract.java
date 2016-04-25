package com.lhadalo.oladahl.autowork.database;

import android.provider.BaseColumns;

/**
 * Created by oladahl on 16-04-21.
 */
public class DatabaseContract {

    public static abstract class UserEntry{
        public static final String TABLE_NAME = "User";
        public static final String USER_ID = "user_id";
        public static final String EMAIL = "email";
        public static final String FIRST_NAME = "firstname";
        public static final String LAST_NAME = "lastname";
    }

    public static abstract class CompanyEntry {
        public static final String TABLE_NAME = "Company";
        public static final String COMPANY_ID = "companyID";
        public static final String COMPANY_MY_SQL_ID = "companymySQLID";
        public static final String USER_ID = "userID";
        public static final String COMPANY_NAME = "companyname";
        public static final String WAGE = "wage";
        public static final String IS_SYNCED = "ISSYNCED";
        public static final String ACTION_TAG = "ACTIONTAG";
    }

    public static abstract class WorkpassEntry {
        public static final String TABLE_NAME = "Workpass";
        public static final String WORKPASS_ID = "workpass_ID";
        public static final String WORKPASS_MY_SQL_ID = "workpass_mySQL_ID";
        public static final String COMPANY_ID = "company_ID";
        public static final String COMPANY_MY_SQL_ID = "company_mySQL_ID";
        public static final String USER_ID = "user_ID";
        public static final String TITLE = "title";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String BREAK_TIME = "break_time";
        public static final String WORKED_HOURS = "worked_hours";
        public static final String SALARY = "salary";
        public static final String NOTE = "note";
        public static final String IS_SYNCED = "IS_SYNCED";
        public static final String ACTION_TAG = "ACTION_TAG";
        public static final String MONTH = "month";
    }



}
