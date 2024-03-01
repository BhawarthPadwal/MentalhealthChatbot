package com.bae.dialogflowbot.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import com.google.firebase.database.ServerValue;
import java.util.HashMap;
import java.util.Map;

public class AddEditNote extends AppCompatActivity {
    //ImageView back_btn;
    CardView add_note_btn;
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

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        call_all_ids();
        //traverse();
        receive_data();
        delete_note();

        add_note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_data_to_upload();
            }
        });

    }
    private void call_all_ids() {
//        back_btn = findViewById(R.id.back_btn_add_note);
        add_note_btn = findViewById(R.id.insert_note_btn);
        title_et = findViewById(R.id.note_title_et);
        content_et = findViewById(R.id.note_content_et);
        pageTitle = findViewById(R.id.add_note_txt);
        delete_note_tv = findViewById(R.id.delete_note_tv);

    }
    private void receive_data() {
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId != null && !docId.isEmpty()) {
            isEditMode = true;
        }

        title_et.setText(title);
        content_et.setText(content);

        if (isEditMode) {
            pageTitle.setText("Edit your Note!");
            delete_note_tv.setVisibility(View.VISIBLE);
        }
    }
    private void set_data_to_upload() {

        String title = title_et.getText().toString();
        String content = content_et.getText().toString();
        //String timestamp = ServerValue.TIMESTAMP;
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", ServerValue.TIMESTAMP);
        if (title == null || title.isEmpty()) {
            title_et.setError("Title is required");
            return;
        }
        Note note = new Note(title, content, map);
        set_note_to_database(note);
    }
    private void set_note_to_database(Note note) {
        if (isEditMode) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Notes").child(currentUser.getUid()).child("myNotes").child(docId);
            if (!note.getTitle().equals(title) || !note.getContent().equals(content)) {
                databaseReference.setValue(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Note updated
                            Toast.makeText(AddEditNote.this, "Note updated Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddEditNote.this, "Failed while updating note!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddEditNote.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("FailureInUpdating","Error: " + e.getMessage());
                    }
                });
            } else {
                finish();
            }

        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Notes").child(currentUser.getUid()).child("myNotes");
            databaseReference.push().setValue(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Note created
                        Toast.makeText(AddEditNote.this, "Note created Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddEditNote.this, "Failed while updating note!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddEditNote.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FailureInCreating","Error: " + e.getMessage());
                }
            });
        }
    }
    private void delete_note() {
        delete_note_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_note_from_database();
            }
        });
    }
    private void delete_note_from_database() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notes").child(currentUser.getUid()).child("myNotes").child(docId);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddEditNote.this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddEditNote.this, "Failed while deleting note!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddEditNote.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FailureInDeleting","Error: " + e.getMessage());
            }
        });

    }
//    private void traverse() {
//        back_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(AddEditNote.this, NotePage.class));
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                finish();
//            }
//        });
//    }
}