package com.example.timetableapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.timetableapp.databinding.ActivityMainBinding;
import com.example.timetableapp.databinding.ActivityOverviewBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Main_Interface {
    public Context mainContext;
    public ActivityMainBinding binding;
    public ActivityOverviewBinding overviewBinding;

    public Task_RecyclerViewAdapter task_RecyclerViewAdapter;
    public int currentTaskPosition;

    public ArrayList<TaskInformation> taskInformation_List = new ArrayList<>(32);
    public ArrayList<TaskInformation> recyclerView_information = new ArrayList<>();
    public ArrayList<ArrayList<TaskInformation>> memory = new ArrayList<>(1024);

    public LocalDate currentDate;
    public LocalDate displayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        overviewBinding = ActivityOverviewBinding.inflate(getLayoutInflater());
        ConstraintLayout viewRoot = binding.getRoot();
        setContentView(viewRoot);

        mainContext = this;
        TaskInformation taskInformation = new TaskInformation();
        taskInformation.setBinding(overviewBinding);

        //MAIN

        // SET SCREEN PARAMS

        // SET MEMORY
        currentDate = LocalDate.now();
        displayDate = currentDate;

        formatDateDisplay(displayDate,0);
        if (memory.isEmpty()) {
            TaskInformation.setEarliestDate(displayDate);
            TaskInformation.setLatestDate(displayDate);
            memory.add(taskInformation_List);
        }

        // PREVIOUS WEEK
        binding.previousButton.setOnClickListener(v -> {
            displayDate = formatDateDisplay(displayDate,-1);

            //CHECK IF NEW DATE
            if (displayDate.isBefore(TaskInformation.getEarliestDate())) {
                ArrayList<TaskInformation> newTaskInformation_List = new ArrayList<>(32);
                memory.add(0, newTaskInformation_List);
                taskInformation_List = newTaskInformation_List;
                TaskInformation.setEarliestDate(displayDate);
            } else { // LOAD DATE FROM MEMORY
                //get location
                int currentDatePos = displayDate.compareTo(TaskInformation.getEarliestDate());
                taskInformation_List = memory.get(currentDatePos);
                //update recycler view
            }
            updateRecyclerViewAdapter(taskInformation_List);
        });

        // NEXT WEEK
        binding.nextButton.setOnClickListener(v -> {
            displayDate = formatDateDisplay(displayDate,1);

            //CHECK IF NEW DATE
            if (displayDate.isAfter(TaskInformation.getLatestDate())) {
                ArrayList<TaskInformation> newTaskInformation_List = new ArrayList<>(32);
                memory.add(newTaskInformation_List);
                taskInformation_List = newTaskInformation_List;
                TaskInformation.setLatestDate(displayDate);

            } else { // LOAD DATE FROM MEMORY
                //get location
                int currentDatePos = displayDate.compareTo(TaskInformation.getEarliestDate());
                taskInformation_List = memory.get(currentDatePos);
            }
            updateRecyclerViewAdapter(taskInformation_List);
        });

        // LOAD FROM MEMORY


        // CREATE NEW TASK
        create_taskGui();
        add_taskListener();
        delete_task();
        edit_task();

        set_taskGuiPosition(taskInformation_List);

        //ADDONS

    }



    private LocalDate formatDateDisplay(LocalDate displayDate, int addDay) {

        displayDate = displayDate.plusDays(addDay);
        String displayDateText = displayDate.getDayOfWeek().toString() + "\n" + displayDate.format(Constants.FORMATTER);
        binding.currentDateTextView.setText(displayDateText);
        return displayDate;
    }

    public void loadDate() {

    }

    @Override
    public void create_taskGui() {
        recyclerView_information.addAll(taskInformation_List);
        task_RecyclerViewAdapter = new Task_RecyclerViewAdapter(mainContext, recyclerView_information);
        binding.taskRecyclerView.setAdapter(task_RecyclerViewAdapter);
        binding.taskRecyclerView.setLayoutManager(new LinearLayoutManager(mainContext));
    }



    @Override
    public void add_taskListener() {
        binding.addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_activity_overview(Constants.ADD_REQUEST_CODE);
            }
            public void open_activity_overview(int request_Code) {
                Intent intent = new Intent(mainContext, OverviewActivity.class);
                intent.putExtra(Constants.ADD_OR_EDIT_KEY, Constants.ADD);
                TaskInformation taskInformation = new TaskInformation();
                taskInformation.setDate(displayDate.format(Constants.FORMATTER));
                intent.putExtra(Constants.OVERVIEW_INFO_KEY, taskInformation);
                startActivityForResult(intent, request_Code);
            }

        });
    }



    @Override
    public void set_taskGuiPosition(List<TaskInformation> taskInformation_List){
        //SORT LIST
        Comparator<TaskInformation> taskInformation_sortByStartTime_Comparator = new Comparator<TaskInformation>() {
            @Override
            public int compare(TaskInformation o1, TaskInformation o2) {
                if (timeString_toInt(o1.getStartTime()) > timeString_toInt(o2.getStartTime())) {
                    return 1;
                } else {
                    return -1;
                }
            }
            private int timeString_toInt(String input) {
                String militaryTime = String.format("%s%s", input.substring(0,2), input.substring(3,5));
                int intValue = Integer.parseInt(militaryTime);
                return intValue;
            }
        };
        Collections.sort(taskInformation_List, taskInformation_sortByStartTime_Comparator);
        //UPDATE LIST
        updateRecyclerViewAdapter(taskInformation_List);
    }

    public void updateRecyclerViewAdapter(List<TaskInformation> taskInformation_List) {
        recyclerView_information.clear();
        task_RecyclerViewAdapter.notifyDataSetChanged();
        recyclerView_information.addAll(taskInformation_List);
        task_RecyclerViewAdapter.notifyItemRangeInserted(0,recyclerView_information.size());
    }

    //EDIT TASK

    @Override
    public void edit_task() {
        task_RecyclerViewAdapter.setOnItemClickListener(new Task_RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //RETRIEVE INFO FROM ITEM CLICK AND THEN EDIT TASK
                currentTaskPosition = position;
                open_activity_overview(Constants.EDIT_REQUEST_CODE);
            }
            public void open_activity_overview(int request_Code) {
                Intent intent = new Intent(mainContext, OverviewActivity.class);
                TaskInformation taskInformation = taskInformation_List.get(currentTaskPosition);
                intent.putExtra(Constants.OVERVIEW_INFO_KEY, taskInformation);
                intent.putExtra(Constants.ADD_OR_EDIT_KEY, Constants.EDIT);
                startActivityForResult(intent, request_Code);
            }
        });
    }

    @Override
    public void delete_task() {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //REMOVE ITEM
                taskInformation_List.remove(viewHolder.getAdapterPosition());
                recyclerView_information.clear();
                task_RecyclerViewAdapter.notifyDataSetChanged();
                recyclerView_information.addAll(taskInformation_List);
                task_RecyclerViewAdapter.notifyItemRangeInserted(0,recyclerView_information.size());
            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.taskRecyclerView);
    }

    //TASK CLICK LISTENER --> OVERVIEW

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.ADD_REQUEST_CODE: {
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    TaskInformation taskInformation = data.getParcelableExtra(Constants.OVERVIEW_INFO_KEY);
                    taskInformation_List.add(taskInformation);
                    set_taskGuiPosition(taskInformation_List);
                    // WHAT IF THE DATE IS CHANGED???!?????????????????????????????????????????
                }
                break;
            }
            case Constants.EDIT_REQUEST_CODE: {
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    TaskInformation oldTaskInformation = taskInformation_List.get(currentTaskPosition);
                    TaskInformation newTaskInformation = data.getParcelableExtra(Constants.OVERVIEW_INFO_KEY);
                    //CHECK IF DATE CHANGED
                    if (oldTaskInformation.getDate() != newTaskInformation.getDate()) {
                        //add day
                        LocalDate newDate = LocalDate.parse(newTaskInformation.getDate(), Constants.FORMATTER);
                        int newDayPos = newDate.compareTo(TaskInformation.getEarliestDate());
                        if(newDayPos >= (memory.size()-1)) {
                            int extraPositiveElements = newDayPos -(memory.size()-1);
                            for (int i = 0; i < extraPositiveElements; i++) {
                                ArrayList<TaskInformation> newTaskInformation_List = new ArrayList<>(32);
                                memory.add(newTaskInformation_List);
                            }
                        } else if (newDayPos < 0){
                            int extraNegativeElements = - newDayPos;
                            for (int i = 0; i <= extraNegativeElements; i++) {
                                ArrayList<TaskInformation> newTaskInformation_List = new ArrayList<>(32);
                                memory.add(0, newTaskInformation_List);
                            }
                            newDayPos = 0;
                        }

                        //add task
                        ArrayList<TaskInformation> newTaskInformation_list = memory.get(newDayPos);
                        set_taskGuiPosition(newTaskInformation_list);
                        memory.get(newDayPos).add(newTaskInformation);

                        //delete task from memory
                        LocalDate oldDate = LocalDate.parse(oldTaskInformation.getDate(), Constants.FORMATTER);
                        int oldDayPos = oldDate.compareTo(TaskInformation.getEarliestDate());
                        memory.get(oldDayPos).remove(oldTaskInformation);

                        //UPDATE LIST
                        int dateChange = newDate.compareTo(displayDate);
                        displayDate = formatDateDisplay(displayDate, dateChange);
                        taskInformation_List = memory.get(newDayPos);
                        updateRecyclerViewAdapter(taskInformation_List);
                        //set new taskInformation_List
                    } else {
                        //CHECK IF OTHER INFO CHANGED
                        if (!compareObjects(oldTaskInformation, newTaskInformation)) {
                            taskInformation_List.set(currentTaskPosition,newTaskInformation);
                            set_taskGuiPosition(taskInformation_List);
                        }
                    }
                }
                break;
            }
        }
    }

    public static Boolean compareObjects(TaskInformation a, TaskInformation b){
        int comparison =  Comparator.comparing(TaskInformation::getTaskName)
                .thenComparing(TaskInformation::getStartTime)
                .thenComparing(TaskInformation::getEndTime)
                .thenComparing(TaskInformation::getDate)
                .thenComparing(TaskInformation::getDetails)
                .compare(a, b);
        if (comparison == 0) {return true;}
        else {return false;}
    }

    public void changeDate(int addDay) {
        displayDate = formatDateDisplay(displayDate,addDay);
        //CHECK IF NEW DATE
        LocalDate newDate = LocalDate.parse(newTaskInformation.getDate(), Constants.FORMATTER);
        int newDayPos = newDate.compareTo(TaskInformation.getEarliestDate());
        if(newDayPos >= (memory.size()-1)) {
            int extraPositiveElements = newDayPos -(memory.size()-1);
            for (int i = 0; i < extraPositiveElements; i++) {
                ArrayList<TaskInformation> newTaskInformation_List = new ArrayList<>(32);
                memory.add(newTaskInformation_List);
            }
        } else if (newDayPos < 0){
            int extraNegativeElements = - newDayPos;
            for (int i = 0; i <= extraNegativeElements; i++) {
                ArrayList<TaskInformation> newTaskInformation_List = new ArrayList<>(32);
                memory.add(0, newTaskInformation_List);
            }
            newDayPos = 0;
        }
        updateRecyclerViewAdapter(taskInformation_List);

    }
}


