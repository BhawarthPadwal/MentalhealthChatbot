package com.bae.dialogflowbot.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.adapters.MusicAdapter;
import com.bae.dialogflowbot.models.RelaxingSounds;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VisualsPage extends AppCompatActivity {    // Podcast Page
    RecyclerView recyclerView;
    MusicAdapter adapter;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visuals_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        call_all_ids();
        set_podcast_track();
    }
    private void call_all_ids() {
        recyclerView = findViewById(R.id.visual_rv);
    }
    private void set_podcast_track() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Podcasts");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<RelaxingSounds> options =
                new FirebaseRecyclerOptions.Builder<RelaxingSounds>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Podcasts"),RelaxingSounds.class)
                        .build();
        adapter = new MusicAdapter(options, this, recyclerView);
        recyclerView.setAdapter(adapter);
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