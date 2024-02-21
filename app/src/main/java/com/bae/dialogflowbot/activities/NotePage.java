package com.bae.dialogflowbot.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.adapters.NoteAdapter;
import com.bae.dialogflowbot.models.Note;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotePage extends AppCompatActivity {
    FloatingActionButton add_note_fab;
    RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;
    NoteAdapter noteAdapter;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_page);

        call_all_ids();
        traverse();
        set_notes();
        set_bottom_navigation();
    }
    private void call_all_ids() {
        add_note_fab = findViewById(R.id.add_note_fab);
        recyclerView = findViewById(R.id.notes_recyclerview);
        bottomNavigationView = findViewById(R.id.bottomNavigation_notes);
    }

    private void set_notes() {

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notes").child(currentUser.getUid()).child("myNotes");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Note> options =
                new FirebaseRecyclerOptions.Builder<Note>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Notes").child(currentUser.getUid()).child("myNotes"),Note.class)
                        .build();

        noteAdapter = new NoteAdapter(options, this);
        recyclerView.setAdapter(noteAdapter);
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                noteArrayList.clear();
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//                    Note note = dataSnapshot.getValue(Note.class);
//                    noteArrayList.add(note);
//                }
//                noteAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }
    private void set_bottom_navigation() {

        bottomNavigationView.setSelectedItemId(R.id.bottom_notes);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(NotePage.this, HomePage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            } else if (item.getItemId() == R.id.bottom_calender) {
                startActivity(new Intent(NotePage.this, TaskPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(NotePage.this, ProfilePage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
            return true;
        });
    }

    private void traverse() {
        add_note_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotePage.this, AddEditNote.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}