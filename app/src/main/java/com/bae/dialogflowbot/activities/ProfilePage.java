package com.bae.dialogflowbot.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bae.dialogflowbot.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePage extends AppCompatActivity {
    ImageView logout_btn, change_pass_btn, edit_profile_btn, profile_picture, feedback_iv;
    TextView userName_tv, userEmail_tv;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    BottomNavigationView bottomNavigationView;
    public static final String TAG = "ProfilePage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        call_all_ids();
        set_pic();
        retrieve_and_set_data();
        set_bottom_navigation();

        edit_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfilePage.this, EditProfilePage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lintent = new Intent(ProfilePage.this, LogoutPage.class);
                lintent.putExtra("FROM_ACTIVITY", "ProfilePage");
                startActivity(lintent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        change_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pintent = new Intent(ProfilePage.this, ForgotPassword.class);
                pintent.putExtra("FROM_ACTIVITY", "ProfilePage");
                startActivity(pintent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        feedback_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    private void call_all_ids() {
        logout_btn = findViewById(R.id.logout_btn_profile);
        change_pass_btn = findViewById(R.id.change_password_profile);
        edit_profile_btn = findViewById(R.id.edit_profile);
        userName_tv = findViewById(R.id.profile_name_tv);
        userEmail_tv = findViewById(R.id.profile_email_tv);
        profile_picture = findViewById(R.id.profile_picture);
        bottomNavigationView = findViewById(R.id.bottomNavigation_profile);
        feedback_iv = findViewById(R.id.feedback_iv);
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Feedback");
        builder.setMessage("Please provide your feedback:");

        // Set up the input
        final EditText input = new EditText(this);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String feedback = input.getText().toString();
                // Process the feedback here (e.g., send it to a server, save it locally, etc.)
                // For example, you can show a toast to confirm the feedback submission
                updateDataToDatabase(feedback);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateDataToDatabase(String feedback) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Feedbacks");
        databaseReference.push().setValue(feedback).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Feedback submitted: " + feedback, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfilePage.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(ProfilePage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieve_and_set_data() {

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("USER_NAME", "");
        String userEmail = sharedPreferences.getString("USER_EMAIL", "");
        String imageUrl = sharedPreferences.getString("IMAGE_URL","");

        userName_tv.setText(userName);
        userEmail_tv.setText(userEmail);

        Glide.with(ProfilePage.this)
                .load(imageUrl)
                .placeholder(R.drawable.profile) // Placeholder image while loading
                .error(R.drawable.profile_error) // Error image if loading fails
                .into(profile_picture);

    }
    private void set_pic() {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserData").child(currentUser.getUid()).child("myData");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String updatedImageURL = snapshot.child("imageUrl").getValue(String.class);
                    if (updatedImageURL != null) {
                        Glide.with(ProfilePage.this)
                                .load(updatedImageURL)
                                .placeholder(R.drawable.profile) // Placeholder image while loading
                                .error(R.drawable.profile_error) // Error image if loading fails
                                .into(profile_picture);
                    } else {
                        profile_picture.setImageResource(R.drawable.profile_error);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfilePage.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error: " + databaseError.getMessage());
            }
        });

    }

    private void set_bottom_navigation() {

        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_notes) {
                startActivity(new Intent(ProfilePage.this, NotePage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            } else if (item.getItemId() == R.id.bottom_calender) {
                startActivity(new Intent(ProfilePage.this, TaskPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            } else if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(ProfilePage.this, HomePage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
            return true;
        });
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}