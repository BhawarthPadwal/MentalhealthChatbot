package com.bae.dialogflowbot.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bae.dialogflowbot.MainActivity;
import com.bae.dialogflowbot.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class HomePage extends AppCompatActivity {
    TextView dailyThoughts, userGreeting;
    BottomNavigationView bottomNavigationView;
    ImageView chatBot, startMed, startSleepMed, startVisuals, startSounds, startStories, startConnections;
    List<String> thoughtList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        call_all_ids();
        set_bottom_navigation();
        traverse();
        set_daily_thought();

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("USER_NAME", "");
        String greetings = generateGreeting();
        String mainGreeting = greetings + ", " + userName + "!";
        userGreeting.setText(mainGreeting);
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
        dailyThoughts = findViewById(R.id.daily_thought);
        userGreeting = findViewById(R.id.user_greetings);
    }
    private void set_daily_thought() {
        thoughtList.add("Embrace the power of positive thinking, for it has the ability to transform your entire day.");
        thoughtList.add("Each day is a new opportunity to learn, grow, and become the person you aspire to be.");
        thoughtList.add("Practice mindfulness by focusing on the present moment and appreciating the beauty of simple pleasures.");
        thoughtList.add("Every challenge you face is an opportunity for growth and self-discovery.");
        thoughtList.add("Cultivate an attitude of gratitude, and watch as it attracts more positivity into your life.");
        thoughtList.add("Take time each day to nourish your mind, body, and soul with activities that bring you joy and fulfillment.");
        thoughtList.add("Remember that setbacks are temporary, but the lessons they teach can last a lifetime.");
        thoughtList.add("Surround yourself with people who uplift and inspire you to become the best version of yourself.");
        thoughtList.add("Treat yourself with kindness and compassion, for you are deserving of love and acceptance just as you are.");
        thoughtList.add("Focus on progress, not perfection, and celebrate the small victories along the way.");
        // Display a random thought from the list
        Random random = new Random();
        int index = random.nextInt(thoughtList.size());
        dailyThoughts.setText(thoughtList.get(index));
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
            }
        });
        startSleepMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, SleepMeditation.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        startSounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, CalmingSoundPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        startVisuals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, VisualsPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        startStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, ArticlesPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        startConnections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, CommunityPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private String generateGreeting() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (hour >= 5 && hour < 12) {
            greeting = "Good morning";
        } else if (hour >= 12 && hour < 17) {
            greeting = "Good afternoon";
        } else if (hour >= 17 && hour < 21) {
            greeting = "Good evening";
        } else {
            greeting = "Good night";
        }

        return greeting;
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