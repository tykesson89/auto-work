package com.lhadalo.oladahl.autowork;

import android.provider.BaseColumns;

/**
 * Created by oladahl on 16-03-28.
 */
public class WorkpassContract {
    public static abstract class WorkpassEntry implements BaseColumns{
        public static final String WORKPASS_ID = "workpass_id";
        public static final String TABLE_NAME = "workpass";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_USER_ID = "userId";
        public static final String COLUMN_WORKPLACE_ID = "workplaceId";
        public static final String COLUMN_START_DATE_TIME = "startdatetime";
        public static final String COLUMN_END_DATE_TIME = "enddatetime";
        public static final String COLUMN_SALARY = "salary";
        public static final String COLUMN_BRAKE_TIME = "braketime";
        public static final String COLUMN_NOTE = "note";
    }
}
