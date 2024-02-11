package com.bae.dialogflowbot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bae.dialogflowbot.R;

public class ProfilePage extends AppCompatActivity {
    ImageView logout_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        call_all_ids();

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfilePage.this, LogoutPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private void call_all_ids() {
        logout_btn = findViewById(R.id.logout_btn_profile);
    }
}