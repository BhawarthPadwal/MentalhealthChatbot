package com.bae.dialogflowbot.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.adapters.TaskAdapter;
import com.bae.dialogflowbot.bottomfragment.AddNewTask;
import com.bae.dialogflowbot.models.Note;
import com.bae.dialogflowbot.models.Task;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TaskPage extends AppCompatActivity {
    FloatingActionButton addTaskFab, completedTaskFab;
    RecyclerView recyclerView;
    TaskAdapter taskAdapter;
    BottomNavigationView bottomNavigationView;
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
        set_bottom_navigation();
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
        bottomNavigationView = findViewById(R.id.bottomNavigation_task);
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

    private void set_bottom_navigation() {

        bottomNavigationView.setSelectedItemId(R.id.bottom_calender);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_notes) {
                startActivity(new Intent(TaskPage.this, NotePage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            } else if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(TaskPage.this, HomePage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(TaskPage.this, ProfilePage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
            return true;
        });
    }


    private void set_add_task() {
        addTaskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
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