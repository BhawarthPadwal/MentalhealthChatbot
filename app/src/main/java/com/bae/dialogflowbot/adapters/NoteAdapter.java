package com.bae.dialogflowbot.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.activities.AddEditNote;
import com.bae.dialogflowbot.models.Note;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import java.text.SimpleDateFormat;
import java.util.Map;

public class NoteAdapter extends FirebaseRecyclerAdapter<Note, NoteAdapter.MyNoteViewHolder> {
    Context context;
    public NoteAdapter(@NonNull FirebaseRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull MyNoteViewHolder holder, int position, @NonNull Note model) {
        holder.title_tv.setText(model.getTitle());
        holder.content_tv.setText(model.getContent());

        holder.timestamp_tv.setText("Loading..."); // Placeholder text while loading
        Map<String, Object> timestampMap = model.getTimestamp(); // Accessing the timestamp from the TimestampModel
        if (timestampMap != null && timestampMap.containsKey("timestamp")) { // If the timestampMap is not null and contains the "timestamp" key
            Object timestampObject = timestampMap.get("timestamp"); // Extracting the timestamp value
            if (timestampObject instanceof Long) { // Converting the timestamp value to a readable format (if needed)
                String timestampString = formatTimestamp((Long) timestampObject);
                holder.timestamp_tv.setText(timestampString); // Setting the timestamp text to your TextView
            } else { // Handle unexpected data type or missing timestamp
                holder.timestamp_tv.setText("Invalid timestamp");
            }
        } else { // Handle missing timestamp
            holder.timestamp_tv.setText("Timestamp not available");
        }

        String docId = this.getRef(position).getKey();
        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, AddEditNote.class);
            intent.putExtra("title",model.getTitle());
            intent.putExtra("content",model.getContent());
            // Passing the timestamp as an extra
            if (timestampMap != null && timestampMap.containsKey("timestamp")) {
                intent.putExtra("timestamp", (Long) timestampMap.get("timestamp"));
            }
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public MyNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_recyclerview_layout, parent, false);
        return new MyNoteViewHolder(view);
    }

    public class MyNoteViewHolder extends RecyclerView.ViewHolder{
        TextView title_tv, content_tv, timestamp_tv;
        public MyNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.note_title_edit_text);
            content_tv = itemView.findViewById(R.id.note_content_edit_text);
            timestamp_tv = itemView.findViewById(R.id.note_timestamp_edit_text);
        }
    }
    private String formatTimestamp(Long timestamp) {
        // Your timestamp formatting logic here
        // For example, you can use SimpleDateFormat to format the timestamp
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(timestamp); // Example: returning as string
    }
}