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
    ArrayList<Note> noteArrayList = new ArrayList<>();
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
    }
    private void call_all_ids() {
        add_note_fab = findViewById(R.id.add_note_fab);
        recyclerView = findViewById(R.id.notes_recyclerview);
    }

    private void set_notes() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notes").child(currentUser.getUid()).child("myNotes");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(this, noteArrayList);
        recyclerView.setAdapter(noteAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noteArrayList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Note note = dataSnapshot.getValue(Note.class);
                    noteArrayList.add(note);
                }
                noteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
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
}