package com.example.fitmatrix_v1.DatabaseOperator;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.fitmatrix_v1.UserDetails;

import java.util.ArrayList;
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

    public void updateUserDetails(int userId, int age, double weight, double height) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.UserEntry.COLUMN_AGE, age);
        values.put(DatabaseContract.UserEntry.COLUMN_WEIGHT, weight);
        values.put(DatabaseContract.UserEntry.COLUMN_HEIGHT, height);

        String selection = DatabaseContract.UserEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        int rowsUpdated = db.update(
                DatabaseContract.UserEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        db.close();

        if (rowsUpdated > 0) {
            // Update successful
            Log.d("UserDatabaseHelper", "User details updated successfully.");
        } else {
            // Update failed or user with specified ID not found
            Log.d("UserDatabaseHelper", "Failed to update user details or user with ID not found.");
        }
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


    @SuppressLint("Range")
    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;
        String[] projection = {
                DatabaseContract.UserEntry._ID
        };
        String selection = DatabaseContract.UserEntry.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(
                DatabaseContract.UserEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(DatabaseContract.UserEntry._ID));
        }

        cursor.close();
        db.close();
        return userId;
    }

    @SuppressLint("Range")
    public UserDetails getUserDetails(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int age = -1;
        double weight = -1;
        double height = -1;
        String[] projection = {
                DatabaseContract.UserEntry.COLUMN_AGE,
                DatabaseContract.UserEntry.COLUMN_WEIGHT,
                DatabaseContract.UserEntry.COLUMN_HEIGHT
        };
        String selection = DatabaseContract.UserEntry.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(
                DatabaseContract.UserEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            age = cursor.getInt(cursor.getColumnIndex(DatabaseContract.UserEntry.COLUMN_AGE));
            weight = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.UserEntry.COLUMN_WEIGHT));
            height = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.UserEntry.COLUMN_HEIGHT));
        }

        UserDetails userDetails = new UserDetails(age, weight, height);
        cursor.close();
        db.close();
        return userDetails;
    }



}
