package com.bae.dialogflowbot.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.models.Data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class EditProfilePage extends AppCompatActivity {
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    ImageView profilePictureIv;
    FloatingActionButton editImageFab;
    Button saveBtn;
    EditText userNameEt, contactEt, ageEt, genderEt;
    TextView userEmailTv;
    String imageURL, userName, userEmail;
    Uri uri;
    public static final String TAG = "EditProfile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        call_all_ids();

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        userName = sharedPreferences.getString("USER_NAME","");
        userEmail = sharedPreferences.getString("USER_EMAIL","");

        userNameEt.setText(userName);
        userEmailTv.setText(userEmail);

        // Basically used for opening new activity for selecting/picking images from gallery
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            profilePictureIv.setImageURI(uri);
                        }
                        else {
                            Toast.makeText(EditProfilePage.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        editImageFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserData();
            }
        });

    }
    private void call_all_ids() {
        profilePictureIv = findViewById(R.id.edit_profile_picture);
        editImageFab = findViewById(R.id.fab);
        saveBtn = findViewById(R.id.edit_profile_btn);
        userNameEt = findViewById(R.id.profile_full_name_edit);
        contactEt = findViewById(R.id.profile_mobile_no_edit);
        ageEt = findViewById(R.id.profile_age_edit);
        genderEt = findViewById(R.id.profile_gender_edit);
        userEmailTv = findViewById(R.id.edit_profile_email);
    }

    private void saveUserData() {

        if (uri == null) {
            Toast.makeText(EditProfilePage.this, "No image selected", Toast.LENGTH_SHORT).show();
            return; // Exit the method if URI is null
        }
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images").child(currentUser.getUid())
                .child(Objects.requireNonNull(uri.getLastPathSegment()));

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfilePage.this);
        builder.setCancelable(false);
        builder.setView(R.layout.process_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get download URL asynchronously
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                // Most imp part of the storing data
                uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri urlImage = task.getResult();
                            imageURL = urlImage.toString();
                            uploadData();
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(EditProfilePage.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //dialog.dismiss();
                Toast.makeText(EditProfilePage.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void uploadData() {

        String userNameChanged = userNameEt.getText().toString();
        String userContact = contactEt.getText().toString();
        String userAge = ageEt.getText().toString();
        String gender = genderEt.getText().toString();

        // Validate other fields
        if (userNameChanged.isEmpty() || userContact.isEmpty() || userAge.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return; // Exit the method
        }

        Data data = new Data(userNameChanged, userEmail, userContact, userAge, gender, imageURL);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserData").child(currentUser.getUid()).child("myData");
        databaseReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditProfilePage.this, "Data saved", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = getSharedPreferences("UpdatedData",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USER_NAME",userNameChanged);
                    editor.putString("USER_EMAIL", userEmail);
                    editor.putString("USER_CONTACT", userContact);
                    editor.putString("USER_AGE", userAge);
                    editor.putString("USER_GENDER", gender);
                    editor.putString("IMAGE_URL", imageURL);
                    editor.apply();

                } else {
                    Toast.makeText(EditProfilePage.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error: " + task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfilePage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error: " + e.getLocalizedMessage());
            }
        });




    }
}