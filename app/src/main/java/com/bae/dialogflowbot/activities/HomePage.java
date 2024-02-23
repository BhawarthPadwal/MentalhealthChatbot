package com.bae.dialogflowbot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bae.dialogflowbot.MainActivity;
import com.bae.dialogflowbot.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView chatBot, startMed, startSleepMed, startVisuals, startSounds, startStories, startConnections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        call_all_ids();
        set_bottom_navigation();
        traverse();
    }
    private void call_all_ids() {
        bottomNavigationView = findViewById(R.id.bottomNavigation_home);
        chatBot = findViewById(R.id.chatbot_icon);
        startMed = findViewById(R.id.meditation_start);
        startSleepMed = findViewById(R.id.sleep_start);
        startVisuals = findViewById(R.id.visuals_start);
        startSounds = findViewById(R.id.sounds_start);
        startStories = findViewById(R.id.stories_start);
        startConnections = findViewById(R.id.community_start);
    }

    private void set_bottom_navigation() {

        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_notes) {
                startActivity(new Intent(HomePage.this, NotePage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            } else if (item.getItemId() == R.id.bottom_calender) {
                startActivity(new Intent(HomePage.this, TaskPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(HomePage.this, ProfilePage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
            return true;
        });
    }
    private void traverse() {
        chatBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        startMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, MeditationPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                Toast.makeText(HomePage.this, "Meditation Page", Toast.LENGTH_SHORT).show();
            }
        });
        startSleepMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, SleepMeditation.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                Toast.makeText(HomePage.this, "Sleep Meditation Page", Toast.LENGTH_SHORT).show();

            }
        });
        startSounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, CalmingSoundPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                Toast.makeText(HomePage.this, "Calming Sound Page", Toast.LENGTH_SHORT).show();
            }
        });
        startVisuals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, VisualsPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                Toast.makeText(HomePage.this, "Calming Sound Page", Toast.LENGTH_SHORT).show();
            }
        });
        startStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, ArticlesPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                Toast.makeText(HomePage.this, "Calming Sound Page", Toast.LENGTH_SHORT).show();
            }
        });
        startConnections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, CommunityPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                Toast.makeText(HomePage.this, "Calming Sound Page", Toast.LENGTH_SHORT).show();
            }
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