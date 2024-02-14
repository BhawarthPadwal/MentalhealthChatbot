package com.bae.dialogflowbot.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.models.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.google.protobuf.Timestamp;



import java.text.SimpleDateFormat;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyNoteViewHolder> {
    Context context;
    ArrayList<Note> noteArrayList;

    public NoteAdapter(Context context, ArrayList<Note> noteArrayList) {
        this.context = context;
        this.noteArrayList = noteArrayList;
    }

    @NonNull
    @Override
    public MyNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_recyclerview_layout, parent, false);
        return new MyNoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyNoteViewHolder holder, int position) {
        Note note = noteArrayList.get(position);
        holder.title_tv.setText(note.getTitle());
        holder.content_tv.setText(note.getContent());
        holder.timestamp_tv.setText(note.getTimestamp());



        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent();
            intent.putExtra("title", note.getTitle());
            intent.putExtra("content",note.getContent());
            intent.putExtra("timestamp",note.getTimestamp());
        });


    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public class MyNoteViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv, content_tv, timestamp_tv;
        public MyNoteViewHolder(@NonNull View itemView) {
            super(itemView);

            title_tv = itemView.findViewById(R.id.note_title_edit_text);
            content_tv = itemView.findViewById(R.id.note_content_edit_text);
            timestamp_tv = itemView.findViewById(R.id.note_timestamp_edit_text);

        }
    }
}
