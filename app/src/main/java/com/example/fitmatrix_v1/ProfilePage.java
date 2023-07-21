package com.example.fitmatrix_v1;

import android.content.Context;
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

import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        UserDetails userDetails = userDatabaseHelper.getUserDetails(userName);

        TextView ageTextView = findViewById(R.id.tv_current_age);
        ageTextView.setText(String.valueOf(userDetails.getAge()));
        TextView weightTextView = findViewById(R.id.tv_current_weight);
        weightTextView.setText(String.valueOf(userDetails.getWeight()));
        TextView heightTextView = findViewById(R.id.tv_current_height);
        heightTextView.setText(String.valueOf(userDetails.getHeight()));


        TextView userNameTextView = findViewById(R.id.tv_user_name);
        userNameTextView.setText(userName);
        profileImageView = findViewById(R.id.img_user_profile);
        defaultImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.demo_profile);
        profileImageView.setImageBitmap(defaultImageBitmap);

        Bitmap savedImageBitmap = loadImageFromInternalStorage();
        if (savedImageBitmap != null) {
            profileImageView.setImageBitmap(savedImageBitmap);
        }


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

                // Save the selected image to internal storage
                saveImageToInternalStorage(selectedImageBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImageToInternalStorage(Bitmap bitmap) {
        // Create a file name for the image
        String fileName = "profile_picture.png";

        try {
            // Open a file output stream to write the image to the internal storage
            FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);

            // Compress the image (you can adjust the quality if needed)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            // Close the output stream
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImageFromInternalStorage() {
        String fileName = "profile_picture.png";

        try {
            // Open a file input stream to read the image from internal storage
            FileInputStream inputStream = openFileInput(fileName);

            // Decode the stream into a Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Close the input stream
            inputStream.close();

            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void goToHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
