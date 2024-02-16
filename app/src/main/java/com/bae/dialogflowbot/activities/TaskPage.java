package com.bae.dialogflowbot.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.adapters.TaskAdapter;
import com.bae.dialogflowbot.bottomfragment.AddNewTask;
import com.bae.dialogflowbot.models.Note;
import com.bae.dialogflowbot.models.Task;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TaskPage extends AppCompatActivity {
    FloatingActionButton addTaskFab, completedTaskFab;
    RecyclerView recyclerView;
    TaskAdapter taskAdapter;
    int itemCount;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_page);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        call_all_ids();
        set_add_task();
        set_task();

//        itemCount = taskAdapter.getItemCount();
//        if (itemCount == 0) {
//            recyclerView.setBackgroundResource(R.layout.);
//        } else {
//            recyclerView.ba;
//        }

        completedTaskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaskPage.this, CompletedTaskPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }
    private void call_all_ids() {
        addTaskFab = findViewById(R.id.add_task_fab);
        completedTaskFab = findViewById(R.id.completed_task_fab);
        recyclerView = findViewById(R.id.task_recyclerview);
    }

    private void set_task() {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tasks").child(currentUser.getUid()).child("myTasks");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Task> options =
                new FirebaseRecyclerOptions.Builder<Task>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Tasks").child(currentUser.getUid()).child("myTasks"),Task.class)
                        .build();

        taskAdapter = new TaskAdapter(options, this);
        recyclerView.setAdapter(taskAdapter);
    }


    private void set_add_task() {
        addTaskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        taskAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        taskAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskAdapter.notifyDataSetChanged();
    }
}