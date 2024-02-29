package com.bae.dialogflowbot.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.models.Task;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class CompletedTaskAdapter extends FirebaseRecyclerAdapter<Task, CompletedTaskAdapter.MyCompletedTaskViewHolder> {
    Context context;
    public CompletedTaskAdapter(@NonNull FirebaseRecyclerOptions<Task> options, Context context) {
        super(options);
        this.context = context;
    }
    DatabaseReference databaseReference;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onBindViewHolder(@NonNull MyCompletedTaskViewHolder holder, int position, @NonNull Task model) {
        holder.comp_title_tv.setText(model.getTitle());
        holder.comp_des_tv.setText(model.getDescription());
        Map<String, Integer> dateData = model.getDate();
        if (dateData != null) {
            Integer dayOfMonth = dateData.get("dayOfMonth"); // Extracting the dayOfMonth value
            Integer month = dateData.get("month"); // Extracting the month value
            Integer year = dateData.get("year"); // Extracting the year value

            String date = "Done on " +dayOfMonth + "/" + month + "/" + year;
            holder.comp_date_tv.setText(date);

        } else {
            holder.comp_date_tv.setText("");
        }
        String docId = this.getRef(position).getKey();
        holder.completed_task.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete Task");
                builder.setMessage("Are you sure you want to delete this task?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onTaskDeletion(docId);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    @NonNull
    @Override
    public MyCompletedTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_task_recyclerview_layout, parent, false);
        return new MyCompletedTaskViewHolder(view);
    }

    public class MyCompletedTaskViewHolder extends RecyclerView.ViewHolder {
        TextView comp_title_tv, comp_des_tv, comp_date_tv;
        LinearLayout completed_task;
        public MyCompletedTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            comp_title_tv = itemView.findViewById(R.id.comp_task_title_tv);
            comp_des_tv = itemView.findViewById(R.id.comp_task_description_tv);
            comp_date_tv = itemView.findViewById(R.id.comp_task_date_tv);
            completed_task = itemView.findViewById(R.id.completed_task_ll);
        }
    }
    private void onTaskDeletion(String docId) {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tasks").child(currentUser.getUid()).child("myCompletedTasks").child(docId);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
