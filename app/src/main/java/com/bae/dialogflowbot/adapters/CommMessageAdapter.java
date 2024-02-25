package com.bae.dialogflowbot.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.models.Community;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Map;

public class CommMessageAdapter extends FirebaseRecyclerAdapter<Community, CommMessageAdapter.MyCommViewHolder> {
    Context context;
    public CommMessageAdapter(@NonNull FirebaseRecyclerOptions<Community> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull CommMessageAdapter.MyCommViewHolder holder, int position, @NonNull Community model) {
        holder.commUser.setText(model.getName());
        holder.commMessage.setText(model.getMessage());

        Glide.with(context).load(model.getImageUrl()).placeholder(R.drawable.profile).error(R.drawable.profile_error).into(holder.commPicture);

        holder.commDate.setText("Loading..."); // Placeholder text while loading
        Map<String, Object> timestampMap = model.getTimestamp(); // Accessing the timestamp from the TimestampModel
        if (timestampMap != null && timestampMap.containsKey("timestamp")) { // If the timestampMap is not null and contains the "timestamp" key
            Object timestampObject = timestampMap.get("timestamp"); // Extracting the timestamp value
            if (timestampObject instanceof Long) { // Converting the timestamp value to a readable format (if needed)
                String timestampString = formatTimestamp((Long) timestampObject);
                holder.commDate.setText(timestampString); // Setting the timestamp text to your TextView
            } else { // Handle unexpected data type or missing timestamp
                holder.commDate.setText("Invalid timestamp");
            }
        } else { // Handle missing timestamp
            holder.commDate.setText("Timestamp not available");
        }
    }

    @NonNull
    @Override
    public CommMessageAdapter.MyCommViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.community_mess_rv_layout, parent, false);
        return new MyCommViewHolder(view);
    }

    public class MyCommViewHolder extends RecyclerView.ViewHolder {
        ImageView commPicture;
        TextView commUser, commMessage, commDate;
        public MyCommViewHolder(@NonNull View itemView) {
            super(itemView);
            commPicture = itemView.findViewById(R.id.comm_profile_picture);
            commUser = itemView.findViewById(R.id.comm_user_name);
            commMessage = itemView.findViewById(R.id.comm_user_message);
            commDate = itemView.findViewById(R.id.comm_date);
        }
    }
    private String formatTimestamp(Long timestamp) {
        // Your timestamp formatting logic here
        // For example, you can use SimpleDateFormat to format the timestamp
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(timestamp); // Example: returning as string
    }
}
