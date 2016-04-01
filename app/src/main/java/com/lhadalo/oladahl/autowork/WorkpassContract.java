package com.lhadalo.oladahl.autowork;

import android.provider.BaseColumns;

/**
 * Created by oladahl on 16-03-28.
 */
public class WorkpassContract {
    public static abstract class WorkpassEntry implements BaseColumns{
        public static final String TABLE_NAME = "tablename";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_WORKPLACE = "workplace";
        public static final String COLUMN_START_DATE = "startdate";
        public static final String COLUMN_START_TIME = "starttime";
        public static final String COLUMN_END_DATE = "enddate";
        public static final String COLUMN_END_TIME = "endtime";
        public static final String COLUMN_BRAKE_TIME = "braketime";
        public static final String COLUMN_NOTE = "note";
    }
}
