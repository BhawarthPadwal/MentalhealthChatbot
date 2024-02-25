package com.bae.dialogflowbot.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.adapters.CommMessageAdapter;
import com.bae.dialogflowbot.models.Community;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class CommunityPage extends AppCompatActivity {
    ImageView commBtnSend;
    EditText commNewMessage;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    CommMessageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        call_all_ids();
        set_sendMessage();
        set_all_comm_messages();
    }
    private void call_all_ids() {
        commBtnSend = findViewById(R.id.community_btnSend);
        commNewMessage = findViewById(R.id.community_editMessage);
        recyclerView = findViewById(R.id.comm_rv);
    }
    private void set_sendMessage() {

        commBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = commNewMessage.getText().toString().trim();
                if (message.isEmpty()) {
                    Toast.makeText(CommunityPage.this, "Please enter your message before posting.", Toast.LENGTH_SHORT).show();
                    commNewMessage.setError("Please fill this field.");
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                    String userName = sharedPreferences.getString("USER_NAME", "");
                    String imageUrl = sharedPreferences.getString("IMAGE_URL","");
                    Map<String, Object> timestamp = new HashMap<>();
                    timestamp.put("timestamp", ServerValue.TIMESTAMP);
                    Community model = new Community(userName, imageUrl, message, timestamp);
                    saveData(model);
                    commNewMessage.setText("");
                }
            }
        });
    }
    private void saveData(Community model) {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("CommunityChats");
        databaseReference.push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CommunityPage.this, "Message Posted.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CommunityPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void set_all_comm_messages() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("CommunityChats");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Community> options =
                new FirebaseRecyclerOptions.Builder<Community>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("CommunityChats"),Community.class)
                        .build();
        adapter = new CommMessageAdapter(options, this);
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