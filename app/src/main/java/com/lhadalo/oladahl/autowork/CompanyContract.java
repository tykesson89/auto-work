package com.lhadalo.oladahl.autowork;

import android.provider.BaseColumns;

/**
 * Created by oladahl on 16-04-21.
 */
public class CompanyContract {
    public static abstract class CompanyEntry{
        private static final String COMPANY_ID = "company_ID";
        private static final String MY_SQL_ID = "mySQL_ID";
        private static final String USER_ID = "user_ID";
        private static final String COMPANY_NAME = "company_name";
        private static final String WAGE = "wage";
        private static final String IS_SYNCED = "IS_SYNCED";
        private static final String ACTION_TAG = "ACTION_TAG";
    }
}
