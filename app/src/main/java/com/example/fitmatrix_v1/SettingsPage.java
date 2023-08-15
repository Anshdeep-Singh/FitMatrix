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

import com.example.fitmatrix_v1.DatabaseOperator.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsPage extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        Button logout_button = findViewById(R.id.btn_logout);

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
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

}