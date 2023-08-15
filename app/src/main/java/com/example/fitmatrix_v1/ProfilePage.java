package com.example.fitmatrix_v1;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ProfilePage extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private Bitmap defaultImageBitmap;
    private Bitmap selectedImageBitmap;

    private FirebaseAuth mAuth;

    private FirebaseStorage mStorage;
    private FirebaseFirestore mFirestore;
    private StorageReference mStorageRef;
    private Uri imageUri;
    private String myUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        mFirestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        TextView userNameTextView = findViewById(R.id.tv_user_name);

        profileImageView = findViewById(R.id.img_user_profile);
        defaultImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.demo_profile);
        profileImageView.setImageBitmap(defaultImageBitmap);


        DocumentReference docRef = mFirestore.collection("users").document(user.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("---->", "DocumentSnapshot data: " + document.getData());
                        userNameTextView.setText(document.getString("name"));
                        if(Objects.equals(document.getString("imageProfile"), " ")){
                            profileImageView.setImageBitmap(defaultImageBitmap);

                        }
                        else{
                            Picasso.get().load(document.getString("imageProfile")).into(profileImageView);

                        }
                    } else {
                        Log.d("---->", "No such document");
                    }
                } else {
                    Log.d("---->", "get failed with ", task.getException());
                }
            }
        });


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
            imageUri = data.getData();
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


        final StorageReference fileRef = mStorageRef.child("photo/" + imageUri.getLastPathSegment());

        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        myUri = uri.toString();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = user.getEmail();
                        mFirestore.collection("users").document(uid).update("imageProfile", myUri);
                        Toast.makeText(ProfilePage.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfilePage.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    });

    }


    public void goToHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
