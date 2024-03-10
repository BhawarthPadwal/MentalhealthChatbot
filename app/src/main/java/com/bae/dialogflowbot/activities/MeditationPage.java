package com.bae.dialogflowbot.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.adapters.MusicAdapter;
import com.bae.dialogflowbot.models.RelaxingSounds;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MeditationPage extends AppCompatActivity {
    RecyclerView recyclerView;
    MusicAdapter adapter;
    DatabaseReference databaseReference;
    FloatingActionButton setTimerFab;
    CountDownTimer countDownTimer;
    TextView countdownTextViewDialog;
    MediaPlayer mediaPlayer;
    long timeLeftInMillis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        call_all_ids();
        set_meditation_track();
        set_timer_func();
        mediaPlayer = MediaPlayer.create(this, R.raw.medi_alarm);
    }
    private void call_all_ids() {
        recyclerView = findViewById(R.id.meditate_track_rv);
        setTimerFab = findViewById(R.id.set_meditation_timer);
    }
    private void set_meditation_track() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Meditation");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<RelaxingSounds> options =
                new FirebaseRecyclerOptions.Builder<RelaxingSounds>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Meditation"),RelaxingSounds.class)
                        .build();
        adapter = new MusicAdapter(options, this, recyclerView);
        recyclerView.setAdapter(adapter);
    }
    private void set_timer_func() {
        setTimerFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });
    }
    private void startTimer(long durationInMillis) {
        timeLeftInMillis = durationInMillis;

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountdownText();
                mediaPlayer.start();
            }
        }.start();
    }
    private void showTimerDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.timer_dialog_layout, null);
        dialogBuilder.setView(dialogView);

        countdownTextViewDialog = dialogView.findViewById(R.id.countdownTextViewDialog);

        final NumberPicker numberPicker = dialogView.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1); // Minimum value for the number picker
        numberPicker.setMaxValue(60); // Maximum value for the number picker

        dialogBuilder.setPositiveButton("Start", null); // Set listener to null initially

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    timeLeftInMillis = 0;
                    updateCountdownText();
                    mediaPlayer.stop();
                }
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        // Override the positive button click listener to start the timer and prevent dialog dismissal
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedMinutes = numberPicker.getValue();
                startTimer(selectedMinutes * 60 * 1000); // Convert minutes to milliseconds
                // You may add additional logic here if needed before dismissing the dialog
                // alertDialog.dismiss(); // Only dismiss the dialog if needed
            }
        });
    }


//    private void showTimerDialog() {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.timer_dialog_layout, null);
//        dialogBuilder.setView(dialogView);
//
//        countdownTextViewDialog = dialogView.findViewById(R.id.countdownTextViewDialog); // Remove TextView declaration here
//
//        final NumberPicker numberPicker = dialogView.findViewById(R.id.numberPicker);
//        numberPicker.setMinValue(1); // Minimum value for the number picker
//        numberPicker.setMaxValue(60); // Maximum value for the number picker
//
//        dialogBuilder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                int selectedMinutes = numberPicker.getValue();
//                startTimer(selectedMinutes * 60 * 1000); // Convert minutes to milliseconds
//            }
//        });
//
//        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                // Cancel button clicked, do nothing
//            }
//        });
//
//        AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.show();
//    }

    private void updateCountdownText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        String timeLeftFormatted = String.format("%02d", seconds);
        countdownTextViewDialog.setText(timeLeftFormatted);
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}