package com.bae.dialogflowbot.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bae.dialogflowbot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private CardView fp_btn;
    private TextInputLayout email_et;
    private FirebaseAuth mAuth;
    private static final String TAG = "Forgot Password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        call_all_ids();
        mAuth = FirebaseAuth.getInstance();
        reset_password_func();
    }
    private void call_all_ids() {
        fp_btn = findViewById(R.id.fp_btn);
        email_et = findViewById(R.id.email_txt_fp);
    }

    private void reset_password_func() {

        fp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_et.getEditText().getText().toString().trim();
                if (email_validator()) {
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                        Toast.makeText(ForgotPassword.this, "Email sent.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ForgotPassword.this, UserLoginPage.class));
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                        finish();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Error: " + e.getMessage());
                                    Toast.makeText(ForgotPassword.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }

    private boolean email_validator() {
        if (email_et.getEditText() == null || email_et.getEditText().getText().toString().trim().isEmpty()) {
            email_et.setError("Please Enter Your Email Id");
            Toast.makeText(this, "Please Enter Your Email Id", Toast.LENGTH_SHORT).show();
            email_et.setFocusable(true);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email_et.getEditText().getText().toString().trim()).matches()) {
            email_et.setError("Please Enter Valid Email Id");
            Toast.makeText(this, "Please Enter Valid Email Id", Toast.LENGTH_SHORT).show();
            email_et.setFocusable(true);
            return false;
        } else {
            email_et.setError(null);
            return true;
        }
    }
}