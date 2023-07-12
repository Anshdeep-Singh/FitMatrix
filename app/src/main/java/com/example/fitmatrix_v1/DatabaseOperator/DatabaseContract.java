package com.example.fitmatrix_v1.DatabaseOperator;

import android.provider.BaseColumns;

public final class DatabaseContract {

    private DatabaseContract() {}

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_HEIGHT = "height";
    }


}
