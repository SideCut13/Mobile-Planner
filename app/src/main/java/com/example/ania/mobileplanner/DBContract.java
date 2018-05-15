package com.example.ania.mobileplanner;

import android.provider.BaseColumns;

public class DBContract {
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "Events.db";

    public static class DBEntry implements BaseColumns{
        public static final String TABLE_NAME = "event";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_NOTIFICATION = "notification";

    }
}
