package com.bae.dialogflowbot.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bae.dialogflowbot.MainActivity;
import com.bae.dialogflowbot.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogoutPage extends AppCompatActivity {
    private Button main_logout_btn;
    private ImageView profilePicture;
    private TextView logout_email;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    public static final String TAG = "LogoutPage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_page);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        call_all_ids();
        set_logout_fun();
        set_pic();
        retrieve_data();


    }
    private void call_all_ids() {
        main_logout_btn = findViewById(R.id.main_logout_btn);
        profilePicture = findViewById(R.id.profile_base_image_logout);
        logout_email = findViewById(R.id.logout_email);
    }

    private void set_pic() {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserData").child(currentUser.getUid()).child("myData");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String updatedImageURL = snapshot.child("imageUrl").getValue(String.class);
                    if (updatedImageURL != null) {
                        Glide.with(LogoutPage.this)
                                .load(updatedImageURL)
                                .placeholder(R.drawable.profile) // Placeholder image while loading
                                .error(R.drawable.profile_error) // Error image if loading fails
                                .into(profilePicture);
                    } else {
                        profilePicture.setImageResource(R.drawable.profile_error);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LogoutPage.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error: " + databaseError.getMessage());
            }
        });

    }

    private void set_logout_fun() {

        main_logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                GoogleSignIn.getClient(LogoutPage.this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                        .signOut()
                        .addOnCompleteListener(LogoutPage.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // After signing out from Google, you can sign in with another account
                                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear().apply();
                                Toast.makeText(LogoutPage.this, "Logged out", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LogoutPage.this, UserLoginPage.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }
                        });
            }
        });

    }
    private void retrieve_data() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("USER_EMAIL", "");
        String imageUrl = sharedPreferences.getString("IMAGE_URL","");

        logout_email.setText(userEmail);

        Glide.with(LogoutPage.this)
                .load(imageUrl)
                .placeholder(R.drawable.profile) // Placeholder image while loading
                .error(R.drawable.profile_error) // Error image if loading fails
                .into(profilePicture);
    }

}