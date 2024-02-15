package com.bae.dialogflowbot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.bottomfragment.AddNewTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TaskPage extends AppCompatActivity {
    FloatingActionButton addTaskFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_page);

        call_all_ids();
        set_add_task();
    }
    private void call_all_ids() {
        addTaskFab = findViewById(R.id.add_task_fab);
    }

    private void set_add_task() {
        addTaskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }
}