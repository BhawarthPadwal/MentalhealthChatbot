package com.bae.dialogflowbot.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.adapters.MusicAdapter;
import com.bae.dialogflowbot.models.RelaxingSounds;
import com.bae.dialogflowbot.models.Task;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CalmingSoundPage extends AppCompatActivity {
    RecyclerView recyclerView;
    MusicAdapter musicAdapter;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calming_sound_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        call_all_ids();
        set_all_music_track();
    }
    private void call_all_ids() {
        recyclerView = findViewById(R.id.sound_track_rv);
    }

    private void set_all_music_track() {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("RelaxingSounds");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<RelaxingSounds> options =
                new FirebaseRecyclerOptions.Builder<RelaxingSounds>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("RelaxingSounds"),RelaxingSounds.class)
                        .build();
        musicAdapter = new MusicAdapter(options, this, recyclerView);
        recyclerView.setAdapter(musicAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        musicAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        musicAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicAdapter.notifyDataSetChanged();
    }

}