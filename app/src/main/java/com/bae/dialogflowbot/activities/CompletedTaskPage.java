package com.bae.dialogflowbot.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.adapters.CompletedTaskAdapter;
import com.bae.dialogflowbot.adapters.TaskAdapter;
import com.bae.dialogflowbot.models.Task;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CompletedTaskPage extends AppCompatActivity {
    RecyclerView recyclerView;
    CompletedTaskAdapter completedTaskAdapter;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_task_page);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        call_all_ids();
        set_completed_task();
    }
    private void call_all_ids() {
        recyclerView = findViewById(R.id.CTask_recyclerview);
    }

    private void set_completed_task() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tasks").child(currentUser.getUid()).child("myCompletedTasks");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Task> options =
                new FirebaseRecyclerOptions.Builder<Task>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Tasks").child(currentUser.getUid()).child("myCompletedTasks"),Task.class)
                        .build();

        completedTaskAdapter = new CompletedTaskAdapter(options, this);
        recyclerView.setAdapter(completedTaskAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        completedTaskAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        completedTaskAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        completedTaskAdapter.notifyDataSetChanged();
    }
}