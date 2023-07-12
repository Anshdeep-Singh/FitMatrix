package com.example.fitmatrix_v1.DatabaseOperator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseContract.UserEntry.TABLE_NAME + " (" +
                    DatabaseContract.UserEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.UserEntry.COLUMN_USERNAME + " TEXT," +
                    DatabaseContract.UserEntry.COLUMN_PASSWORD + " TEXT," +
                    DatabaseContract.UserEntry.COLUMN_AGE + " INTEGER," +
                    DatabaseContract.UserEntry.COLUMN_WEIGHT + " REAL," +
                    DatabaseContract.UserEntry.COLUMN_HEIGHT + " REAL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseContract.UserEntry.TABLE_NAME;

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @SuppressLint("Range")
    public String getUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        String userName = null;
        Cursor cursor = db.query(DatabaseContract.UserEntry.TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                userName = cursor.getString(cursor.getColumnIndex(DatabaseContract.UserEntry.COLUMN_USERNAME));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userName;
    }
}
