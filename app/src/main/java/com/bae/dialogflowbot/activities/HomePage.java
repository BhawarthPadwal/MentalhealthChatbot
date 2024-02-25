package com.bae.dialogflowbot.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Timer;
import java.util.TimerTask;

public class HomePage extends AppCompatActivity {
    TextView dailyThoughts, userGreeting;
    BottomNavigationView bottomNavigationView;
    ImageView chatBot, startMed, startSleepMed, startVisuals, startSounds, startStories, startConnections;
    DatabaseReference databaseReference;
    List<String> thoughtList = new ArrayList<>();
    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

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
        databaseReference = FirebaseDatabase.getInstance().getReference().child("DailyThoughts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String thought = childSnapshot.getValue(String.class);
                    thoughtList.add(thought);
                }
                // Display the first text
                if (!thoughtList.isEmpty()) {
                    dailyThoughts.setText(thoughtList.get(currentIndex));
                }
                // Schedule text updates every 24 hours
                scheduleTextUpdates();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomePage.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void scheduleTextUpdates() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateText();
                    }
                });
            }
        }, 24 * 60 * 60 * 1000, 24 * 60 * 60 * 1000); // 24 hours interval
    }
    private void updateText() {
        // Increment index to get the next text
        currentIndex = (currentIndex + 1) % thoughtList.size();
        // Update TextView with the next text
        dailyThoughts.setText(thoughtList.get(currentIndex));
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
                Toast.makeText(HomePage.this, "Podcast Page", Toast.LENGTH_SHORT).show();
            }
        });
        startStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, ArticlesPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                Toast.makeText(HomePage.this, "Article Page", Toast.LENGTH_SHORT).show();
            }
        });
        startConnections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, CommunityPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                Toast.makeText(HomePage.this, "Community Page", Toast.LENGTH_SHORT).show();
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