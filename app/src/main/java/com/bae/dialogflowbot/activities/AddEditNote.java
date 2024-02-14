package com.bae.dialogflowbot.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.models.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;

public class AddEditNote extends AppCompatActivity {
    ImageView back_btn;
    Button add_note_btn;
    EditText title_et, content_et;
    TextView pageTitle, delete_note_tv;
    String title, content, docId;
    boolean isEditMode = false;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        call_all_ids();
        traverse();

        add_note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_data_to_upload();
            }
        });

    }
    private void call_all_ids() {
        back_btn = findViewById(R.id.back_btn_add_note);
        add_note_btn = findViewById(R.id.insert_note_btn);
        title_et = findViewById(R.id.note_title_et);
        content_et = findViewById(R.id.note_content_et);
        pageTitle = findViewById(R.id.add_note_txt);
        delete_note_tv = findViewById(R.id.delete_note_tv);

    }

    private void set_data_to_upload() {

        String title = title_et.getText().toString();
        String content = content_et.getText().toString();
        String timestamp = ""+System.currentTimeMillis();
        if (title == null || title.isEmpty()) {
            title_et.setError("Title is required");
            return;
        }
//        Note note = new Note();
//        note.setTitle(title);
//        note.setContent(content);
//        note.setContent("Date");
        //note.setTimestamp(new Timestamp(System.currentTimeMillis()));

        Note note = new Note(title, content, timestamp);
        set_note_to_database(note);

    }

    private void set_note_to_database(Note note) {

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Notes").child(currentUser.getUid()).child("myNotes").push().setValue(note)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddEditNote.this, "Note Saved Successfully", Toast.LENGTH_SHORT).show();
                        title_et.setText("");
                        content_et.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddEditNote.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Failure","Error: " + e.getMessage());
                    }
                });


    }

    private void traverse() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddEditNote.this, NotePage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });
    }
}