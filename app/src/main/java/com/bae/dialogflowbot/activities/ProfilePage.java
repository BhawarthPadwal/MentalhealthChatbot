package com.bae.dialogflowbot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bae.dialogflowbot.R;

public class ProfilePage extends AppCompatActivity {
    ImageView logout_btn, change_pass_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        call_all_ids();

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lintent = new Intent(ProfilePage.this, LogoutPage.class);
                lintent.putExtra("FROM_ACTIVITY","ProfilePage");
                startActivity(lintent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        change_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pintent = new Intent(ProfilePage.this, ForgotPassword.class);
                pintent.putExtra("FROM_ACTIVITY","ProfilePage");
                startActivity(pintent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private void call_all_ids() {
        logout_btn = findViewById(R.id.logout_btn_profile);
        change_pass_btn = findViewById(R.id.change_password_profile);
    }
}