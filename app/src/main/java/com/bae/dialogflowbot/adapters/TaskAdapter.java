package com.bae.dialogflowbot.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.bottomfragment.AddNewTask;
import com.bae.dialogflowbot.models.Task;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Map;

public class TaskAdapter extends FirebaseRecyclerAdapter<Task, TaskAdapter.MyTaskViewHolder> {
    Context context;
    public TaskAdapter(@NonNull FirebaseRecyclerOptions<Task> options, Context context) {
        super(options);
        this.context = context;
    }
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference;
    @Override
    protected void onBindViewHolder(@NonNull MyTaskViewHolder holder, int position, @NonNull Task model) {
        holder.taskTitle_tv.setText(model.getTitle());
        holder.taskDesc_tv.setText(model.getDescription());

        Map<String, Integer> dateData = model.getDate();
        if (dateData != null) {
            Integer dayOfMonth = dateData.get("dayOfMonth"); // Extracting the dayOfMonth value
            Integer month = dateData.get("month"); // Extracting the month value
            Integer year = dateData.get("year"); // Extracting the year value
            Integer hourOfDay = dateData.get("hourOfDay"); // Extracting the hourOfDay value
            Integer minute = dateData.get("minute"); // Extracting the minute value
            Integer dayOfWeek = dateData.get("dayOfWeek"); // Extracting the week value

            String dueTime = "at " + hourOfDay + ":" + minute;
            String date = String.valueOf(dayOfMonth);
            holder.taskTime_tv.setText(dueTime);
            holder.taskDay_tv.setText(date);
            String selectedMonth = getMonthAbbreviation(month);
            holder.taskMonth.setText(selectedMonth);
            String selectedWeek = getWeekDayAbbreviation(dayOfWeek);
            holder.taskDayOfWeek.setText(selectedWeek);

            int status = compareDueDate(year, month, dayOfMonth, hourOfDay, minute);

            if (status == 1) {
                holder.taskStatus_tv.setText("Upcoming");
            } else if (status == 0) {
                holder.taskStatus_tv.setText("Overdue");
            } else {
                holder.taskStatus_tv.setText("Now");
            }

        } else {
            holder.taskDayOfWeek.setText("");
            holder.taskDay_tv.setText("");
            holder.taskMonth.setText("");
        }
        String docId = this.getRef(position).getKey();
        holder.menu_btn.setOnClickListener((v)->{
            Toast.makeText(context, "Menu clicked", Toast.LENGTH_SHORT).show();
            PopupMenu popupMenu = new PopupMenu(context, holder.menu_btn);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.task_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.task_done) {
                        Toast.makeText(context, "Task Completed", Toast.LENGTH_SHORT).show();
                        onTaskCompletion(docId, model);
                        //Log.d("DocId",docId);
                        return true;
                    } else if (menuItem.getItemId() == R.id.task_delete) {
                        Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show();
                        onTaskDeletion(docId);
                        return true;
                    }
                    return false;
                }
            });
            popupMenu.show();

        });



    }

    @NonNull
    @Override
    public MyTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_recyclerview_layout, parent, false);
        return new MyTaskViewHolder(view);
    }

    public class MyTaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle_tv, taskDesc_tv, taskTime_tv, taskStatus_tv, taskDayOfWeek, taskDay_tv, taskMonth;
        ImageView menu_btn;
        public MyTaskViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTitle_tv = itemView.findViewById(R.id.task_title_tv);
            taskDesc_tv = itemView.findViewById(R.id.task_description_tv);
            taskTime_tv = itemView.findViewById(R.id.task_time_tv);
            taskStatus_tv = itemView.findViewById(R.id.task_status_tv);
            taskDayOfWeek = itemView.findViewById(R.id.task_dayOfWeek);
            taskDay_tv = itemView.findViewById(R.id.task_dayOfMonth);
            taskMonth = itemView.findViewById(R.id.task_month);
            menu_btn = itemView.findViewById(R.id.task_options);
        }
    }
    private String getMonthAbbreviation(int month) {
        String[] monthsAbbreviation = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        // Ensure that the month number is within the valid range
        if (month >= 1 && month <= 12) {
            return monthsAbbreviation[month - 1];
        } else {
            return "Invalid Month";
        }
    }
    private String getWeekDayAbbreviation(int dayOfWeek) {
        String[] weekDaysAbbreviation = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        // Ensure that the day of the week is within the valid range
        if (dayOfWeek >= 1 && dayOfWeek <= 7) {
            return weekDaysAbbreviation[dayOfWeek - 1];
        } else {
            return "Invalid Day";
        }
    }
    private int compareDueDate(Integer year, Integer month, Integer dayOfMonth, Integer hourOfDay, Integer minute) {
        Calendar currentDateTime = Calendar.getInstance() ;
        Calendar selectedDateTime = Calendar.getInstance();
        selectedDateTime.set(year, month, dayOfMonth, hourOfDay, minute);

        if (selectedDateTime.before(currentDateTime)) {
            return 0;
        } else if (selectedDateTime.after(currentDateTime)) {
            return 1;
        } else {
            return 10;
        }

    }
    private void onTaskCompletion(String docId, Task task) {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tasks").child(currentUser.getUid()).child("myCompletedTasks");
        databaseReference.push().setValue(task).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                Toast.makeText(context, "Task appended successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CompletedTask","Error: "+e.getMessage());
            }
        });

        onTaskDeletion(docId);

    }
    private void onTaskDeletion(String docId) {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tasks").child(currentUser.getUid()).child("myTasks").child(docId);
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

//    public int getItemCount() {
//        return super.getItemCount();
//    }


}
