package com.bae.dialogflowbot.bottomfragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.models.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddNewTask extends BottomSheetDialogFragment {
    private TextView setDueDate;
    private EditText newTaskEt, taskDescription;
    private Button addTaskBtn;
    private String dueDate = "";
    private String description = "";
    private Context context;
    public static final String TAG = "AddNewTask";
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private Map<String, Integer> dateData;
    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_task_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        setDueDate = view.findViewById(R.id.set_due_date);
        newTaskEt = view.findViewById(R.id.add_task_et);
        addTaskBtn = view.findViewById(R.id.addTaskBtn);
        taskDescription = view.findViewById(R.id.add_task_description_et);

        newTaskEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.equals("")) {
                    addTaskBtn.setEnabled(false);
                } else {
                    addTaskBtn.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(context);
            }
        });

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task = newTaskEt.getText().toString();
                description = taskDescription.getText().toString();
                if (task.isEmpty()) {
                    Toast.makeText(context, "Please Enter Your Task!", Toast.LENGTH_SHORT).show();
                }else if (dateData == null) {
                    Toast.makeText(context, "Please Select Your Date & Time!", Toast.LENGTH_SHORT).show();
                } else {
                    Task taskModel = new Task(task, description, dateData, 0);
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Tasks").child(currentUser.getUid()).child("myTasks");
                    databaseReference.push().setValue(taskModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Task Saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG,"Error: " +e.getMessage());
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                dismiss();
            }
        });
    }

    private void datePicker(Context context) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month - 1, dayOfMonth);
                int dayOfWeek = selectedDate.get(Calendar.DAY_OF_WEEK);
                timePicker(context, dayOfMonth, month, year, dayOfWeek);
            }
        },year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void timePicker (Context context, int dayOfMonth, int month, int year, int dayOfWeek) {
        final Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                setDueDate.setText(dayOfMonth+"/"+month+"/"+year+" at "+hourOfDay+":"+minute);
                dueDate = dayOfMonth + "/" +month+ "/" +year+ " " +hourOfDay+ ":" +minute;
                dateData = new HashMap<>();
                dateData.put("dayOfMonth", dayOfMonth);
                dateData.put("dayOfWeek", dayOfWeek);
                dateData.put("month", month);
                dateData.put("year", year);
                dateData.put("hourOfDay", hourOfDay);
                dateData.put("minute", minute);
            }
        }, hourOfDay, minute, true);
        timePickerDialog.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
