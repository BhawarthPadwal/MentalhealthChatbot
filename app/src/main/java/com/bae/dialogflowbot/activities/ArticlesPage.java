package com.bae.dialogflowbot.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.adapters.ArticleAdapter;
import com.bae.dialogflowbot.adapters.MusicAdapter;
import com.bae.dialogflowbot.models.Articles;
import com.bae.dialogflowbot.models.RelaxingSounds;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ArticlesPage extends AppCompatActivity {
    RecyclerView recyclerView;
    ArticleAdapter adapter;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        call_all_ids();
        set_article_track();
    }
    private void call_all_ids() {
        recyclerView = findViewById(R.id.article_rv);
    }
    private void set_article_track() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Articles");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Articles> options =
                new FirebaseRecyclerOptions.Builder<Articles>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Articles"),Articles.class)
                        .build();
        adapter = new ArticleAdapter(options, this);
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