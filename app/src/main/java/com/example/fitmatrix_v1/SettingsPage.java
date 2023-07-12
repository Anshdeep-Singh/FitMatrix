package com.example.fitmatrix_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    // reset database [done]
    // import database
    // export database
    // logout [done]
}