package com.example.fitmatrix_v1;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitmatrix_v1.DatabaseOperator.DatabaseContract;
import com.example.fitmatrix_v1.DatabaseOperator.UserDatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new UserDatabaseHelper(this);
//        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 1, 2);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseContract.UserEntry.TABLE_NAME, null);
        boolean userExists = cursor.moveToFirst();
        cursor.close();
        db.close();

        if (userExists) {
            showLoginView();
        } else {
            showRegistrationPopup();
        }
    }

    private void showLoginView() {

        setContentView(R.layout.activity_login);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (validateCredentials(username, password)) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateCredentials(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseContract.UserEntry.COLUMN_USERNAME
        };
        String selection = DatabaseContract.UserEntry.COLUMN_USERNAME + " = ? AND " +
                DatabaseContract.UserEntry.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(
                DatabaseContract.UserEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    private void showRegistrationPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("User Registration");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_registration, null);
        builder.setView(dialogView);

        final EditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        final EditText passwordEditText = dialogView.findViewById(R.id.passwordEditText);
        final EditText ageEditText = dialogView.findViewById(R.id.ageEditText);
        final EditText weightEditText = dialogView.findViewById(R.id.weightEditText);
        final EditText heightEditText = dialogView.findViewById(R.id.heightEditText);

        builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String ageString = ageEditText.getText().toString();
                String weightString = weightEditText.getText().toString();
                String heightString = heightEditText.getText().toString();

                if (isRegistrationInputValid(name, password, ageString, weightString, heightString)) {
                    int age = Integer.parseInt(ageString);
                    int weight = Integer.parseInt(weightString);
                    int height = Integer.parseInt(heightString);

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DatabaseContract.UserEntry.COLUMN_USERNAME, name);
                    values.put(DatabaseContract.UserEntry.COLUMN_PASSWORD, password);
                    values.put(DatabaseContract.UserEntry.COLUMN_AGE, age);
                    values.put(DatabaseContract.UserEntry.COLUMN_WEIGHT, weight);
                    values.put(DatabaseContract.UserEntry.COLUMN_HEIGHT, height);
                    db.insert(DatabaseContract.UserEntry.TABLE_NAME, null, values);
                    db.close();

                    showLoginView();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid registration input", Toast.LENGTH_SHORT).show();
                    showRegistrationPopup();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean isRegistrationInputValid(String name, String password, String ageString, String weightString, String heightString) {
        if (name.isEmpty() || password.isEmpty() || ageString.isEmpty() || weightString.isEmpty() || heightString.isEmpty()) {
            return false;
        }

        try {
            int age = Integer.parseInt(ageString);
            if (age <= 0 || age > 80) {
                return false;
            }
            double weight = Double.parseDouble(weightString);
            if (weight <= 0 || weight > 600) {
                return false;
            }

            double height = Double.parseDouble(heightString);
            if (height <= 0 || height > 300) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
