package com.example.fitmatrix_v1.DatabaseOperator;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fitmatrix.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "workout";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EXERCISE = "exercise";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_REPS = "reps";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_SET = "set_no";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " TEXT, "
                + COLUMN_EXERCISE + " TEXT, "
                + COLUMN_WEIGHT + " REAL, "
                + COLUMN_SET + " INTEGER, "
                + COLUMN_REPS + " INTEGER, "
                + COLUMN_UNIT + " TEXT)";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

    public long insertData(String id, String exercise, float weight, int set_no ,int reps, String unit) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_EXERCISE, exercise);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_SET, set_no);
        values.put(COLUMN_REPS, reps);
        values.put(COLUMN_UNIT, unit);

        long newRowId = db.insert(TABLE_NAME, null, values);
        db.close();

        return newRowId;
    }

    public List<ExerciseDetails> getAllData() {

        List<ExerciseDetails> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String exercise = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE));
                @SuppressLint("Range") float weight = cursor.getFloat(cursor.getColumnIndex(COLUMN_WEIGHT));
                @SuppressLint("Range") int set_no = cursor.getInt(cursor.getColumnIndex(COLUMN_SET));
                @SuppressLint("Range") int reps = cursor.getInt(cursor.getColumnIndex(COLUMN_REPS));
                @SuppressLint("Range") String unit = cursor.getString(cursor.getColumnIndex(COLUMN_UNIT));

                ExerciseDetails data = new ExerciseDetails(id, exercise, weight, set_no ,reps, unit);
                dataList.add(data);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return dataList;
    }

    public int updateData(String id, String exercise, float weight, int reps, String unit) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EXERCISE, exercise);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_REPS, reps);
        values.put(COLUMN_UNIT, unit);

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {id};

        int numRowsUpdated = db.update(TABLE_NAME, values, selection, selectionArgs);

        db.close();
        return numRowsUpdated;
    }

    public int deleteData(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {id};
        int numRowsDeleted = db.delete(TABLE_NAME, selection, selectionArgs);

        db.close();

        return numRowsDeleted;
    }
}
