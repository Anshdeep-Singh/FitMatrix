package com.example.fitmatrix_v1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitmatrix_v1.DatabaseOperator.DatabaseHelper;
import com.example.fitmatrix_v1.DatabaseOperator.UserDatabaseHelper;

import java.io.IOException;

public class ProfilePage extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private Bitmap defaultImageBitmap;
    private Bitmap selectedImageBitmap;
    private UserDatabaseHelper userDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        userDatabaseHelper = new UserDatabaseHelper(this);
        String userName = userDatabaseHelper.getUser();
        TextView userNameTextView = findViewById(R.id.tv_user_name);
        userNameTextView.setText(userName);
        profileImageView = findViewById(R.id.img_user_profile);
        defaultImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.demo_profile);
        profileImageView.setImageBitmap(defaultImageBitmap);


        TextView editPictureTextView = findViewById(R.id.tv_edit_picture);
        editPictureTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImageView.setImageBitmap(selectedImageBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void goToHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
