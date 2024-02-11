package com.bae.dialogflowbot.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bae.dialogflowbot.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutPage extends AppCompatActivity {
    Button main_logout_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_page);

        call_all_ids();
        set_logout_fun();
    }
    private void call_all_ids() {
        main_logout_btn = findViewById(R.id.main_logout_btn);
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
                                Toast.makeText(LogoutPage.this, "Logged out", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LogoutPage.this, UserLoginPage.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }
                        });
            }
        });

    }
}