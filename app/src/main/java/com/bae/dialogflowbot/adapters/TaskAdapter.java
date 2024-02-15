package com.bae.dialogflowbot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.models.Task;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.Calendar;
import java.util.Map;

public class TaskAdapter extends FirebaseRecyclerAdapter<Task, TaskAdapter.MyTaskViewHolder> {
    Context context;
    public TaskAdapter(@NonNull FirebaseRecyclerOptions<Task> options, Context context) {
        super(options);
        this.context = context;
    }

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



    }

    @NonNull
    @Override
    public MyTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_recyclerview_layout, parent, false);
        return new MyTaskViewHolder(view);
    }

    public class MyTaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle_tv, taskDesc_tv, taskTime_tv, taskStatus_tv, taskDayOfWeek, taskDay_tv, taskMonth;
        public MyTaskViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTitle_tv = itemView.findViewById(R.id.task_title_tv);
            taskDesc_tv = itemView.findViewById(R.id.task_description_tv);
            taskTime_tv = itemView.findViewById(R.id.task_time_tv);
            taskStatus_tv = itemView.findViewById(R.id.task_status_tv);
            taskDayOfWeek = itemView.findViewById(R.id.task_dayOfWeek);
            taskDay_tv = itemView.findViewById(R.id.task_dayOfMonth);
            taskMonth = itemView.findViewById(R.id.task_month);
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

}
