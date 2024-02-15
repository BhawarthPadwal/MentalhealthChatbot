package com.bae.dialogflowbot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bae.dialogflowbot.R;

public class ProfilePage extends AppCompatActivity {
    ImageView logout_btn, change_pass_btn, edit_profile_btn;
    TextView userName_tv, userEmail_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        call_all_ids();

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userName= sharedPreferences.getString("USER_NAME", "");
        String userEmail = sharedPreferences.getString("USER_EMAIL","");

        userName_tv.setText(userName);
        userEmail_tv.setText(userEmail);

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
        edit_profile_btn = findViewById(R.id.edit_profile);
        userName_tv = findViewById(R.id.profile_name_tv);
        userEmail_tv = findViewById(R.id.profile_email_tv);
    }
}