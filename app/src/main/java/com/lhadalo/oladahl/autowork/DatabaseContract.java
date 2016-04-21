package com.lhadalo.oladahl.autowork;

import android.provider.BaseColumns;

/**
 * Created by oladahl on 16-04-21.
 */
public class DatabaseContract {

    public static abstract class UserEntry{
        public static final String TABLE_NAME = ""
        public static final String USER_ID = "user_id";
        public static final String EMAIL = "email";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
    }

    public static abstract class CompanyEntry {
        public static final String COMPANY_ID = "company_ID";
        public static final String COMPANY_MY_SQL_ID = "company_mySQL_ID";
        public static final String USER_ID = "user_ID";
        public static final String COMPANY_NAME = "company_name";
        public static final String WAGE = "wage";
        public static final String IS_SYNCED = "IS_SYNCED";
        public static final String ACTION_TAG = "ACTION_TAG";
    }

    public static abstract class WorkpassEntry {
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
        public static final String IS_SYNCED = "IS_SYNCED";
        public static final String ACTION_TAG = "ACTION_TAG";
    }

}
