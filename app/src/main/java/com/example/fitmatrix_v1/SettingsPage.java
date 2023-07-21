package com.example.fitmatrix_v1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitmatrix_v1.DatabaseOperator.DatabaseContract;
import com.example.fitmatrix_v1.DatabaseOperator.DatabaseHelper;
import com.example.fitmatrix_v1.DatabaseOperator.UserDatabaseHelper;

public class SettingsPage extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private UserDatabaseHelper userDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        Button logout_button = findViewById(R.id.btn_logout);
        Button edit_details = findViewById(R.id.btn_edit_details);

        edit_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsPage.this);
                builder.setTitle("Edit Details");
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.popup_edit_details, null);
                builder.setView(dialogView);
                final EditText ageEditText = dialogView.findViewById(R.id.et_edit_age);
                final EditText weightEditText = dialogView.findViewById(R.id.et_edit_weight);
                final EditText heightEditText = dialogView.findViewById(R.id.et_edit_height);


                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String ageString = ageEditText.getText().toString();
                        String weightString = weightEditText.getText().toString();
                        String heightString = heightEditText.getText().toString();

                        if (isRegistrationInputValid(ageString, weightString, heightString)) {
                            int age = Integer.parseInt(ageString);
                            int weight = Integer.parseInt(weightString);
                            int height = Integer.parseInt(heightString);

                            userDatabaseHelper = new UserDatabaseHelper(SettingsPage.this);
                            ContentValues values = new ContentValues();

                            userDatabaseHelper.updateUserDetails(userDatabaseHelper.getUserId(userDatabaseHelper.getUser()),age,weight,height);

                        } else {
                            Toast.makeText(SettingsPage.this, "Invalid details.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsPage.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public void resetDatabase(View view) {
        databaseHelper = new DatabaseHelper(this);
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        databaseHelper.onUpgrade(db,1,2);
        db.close();

        userDatabaseHelper = new UserDatabaseHelper(this);
        final SQLiteDatabase db2 = userDatabaseHelper.getWritableDatabase();
        userDatabaseHelper.onUpgrade(db2,1,2);
        db2.close();

        Intent intent = new Intent(this, SplashScreenActivity.class);
        startActivity(intent);
    }


    public void goToHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean isRegistrationInputValid( String ageString, String weightString, String heightString) {
        if (ageString.isEmpty() || weightString.isEmpty() || heightString.isEmpty()) {
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

    // reset database [done]
    // import database
    // export database
    // logout [done]
}