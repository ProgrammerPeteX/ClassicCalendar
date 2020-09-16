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

import com.example.timetableapp.databinding.ActivityMainBinding;
import com.example.timetableapp.databinding.ActivityOverviewBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Main_Interface {
    public Context mainContext;
    public ActivityMainBinding binding;
    public ActivityOverviewBinding overviewBinding;

    public Task_RecyclerViewAdapter task_RecyclerViewAdapter;
    public int currentTaskPosition;

    public LocalDate currentDate;
    public LocalDate displayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        overviewBinding = ActivityOverviewBinding.inflate(getLayoutInflater());
        ConstraintLayout viewRoot = binding.getRoot();
        setContentView(viewRoot);
        setTitle("Calendar");

        mainContext = this;
        TaskInformation taskInformation = new TaskInformation();
        taskInformation.setBinding(overviewBinding);
        TaskMemory.setBINDING(binding);

        //MAIN

        // SET SCREEN PARAMS

        // SET MEMORY
        currentDate = LocalDate.now();
        displayDate = currentDate;

        // SET DATE
        TaskMemory.setDisplayDate(displayDate);
        set_PreviousButton_OnClickListener();
        set_NextButton_OnClickListener();

        // CREATE NEW TASK
        create_taskGui();
        add_taskListener();
        delete_task();
        edit_task();

        set_taskGuiPosition(TaskMemory.getTASK_LIST());

        //ADDONS

    }

    public void set_PreviousButton_OnClickListener() {
        // PREVIOUS DATE
        binding.previousButton.setOnClickListener(v -> {
            int subtractDay = -1;
           updateDate(subtractDay);
        });
    }
    public void set_NextButton_OnClickListener() {
        // NEXT DATE
        binding.nextButton.setOnClickListener(v -> {
            int addDay = 1;
            updateDate(addDay);
        });
    }

    public void updateDate(int addDay) {
        displayDate = TaskMemory.changeDisplayDate(displayDate, addDay);
        int MEMORY_index = TaskMemory.getMEMORY_INDEX(displayDate);
        TaskMemory.setTASK_LIST(TaskMemory.getMEMORY().get(MEMORY_index));
        updateRecyclerViewAdapter(TaskMemory.getTASK_LIST());
    }

    @Override
    public void create_taskGui() {
        TaskMemory.getRECYCLERVIEW_LIST().addAll(TaskMemory.getTASK_LIST());
        task_RecyclerViewAdapter = new Task_RecyclerViewAdapter(mainContext, TaskMemory.getRECYCLERVIEW_LIST());
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
                taskInformation.setDateText(displayDate.format(Constants.FORMATTER));
                intent.putExtra(Constants.OVERVIEW_INFO_KEY, taskInformation);
                startActivityForResult(intent, request_Code);
            }

        });
    }



    @Override
    public void set_taskGuiPosition(ArrayList<TaskInformation> taskInformation_List){
        //SORT LIST
        taskInformation_List = TaskMemory.sort_taskList(taskInformation_List);
        //UPDATE LIST
        updateRecyclerViewAdapter(taskInformation_List);
    }

    public void updateRecyclerViewAdapter(List<TaskInformation> taskInformation_List) {
        TaskMemory.getRECYCLERVIEW_LIST().clear();
        task_RecyclerViewAdapter.notifyDataSetChanged();
        TaskMemory.getRECYCLERVIEW_LIST().addAll(taskInformation_List);
        task_RecyclerViewAdapter.notifyItemRangeInserted(0,TaskMemory.getRECYCLERVIEW_LIST().size());
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
                TaskInformation taskInformation = TaskMemory.getTASK_LIST().get(currentTaskPosition);
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
                int positionStart = 0;
                TaskMemory.getTASK_LIST().remove(viewHolder.getAdapterPosition());
                TaskMemory.getRECYCLERVIEW_LIST().clear();
                task_RecyclerViewAdapter.notifyDataSetChanged();
                TaskMemory.getRECYCLERVIEW_LIST().addAll(TaskMemory.getTASK_LIST());
                task_RecyclerViewAdapter.notifyItemRangeInserted(positionStart,TaskMemory.getRECYCLERVIEW_LIST().size());
            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.taskRecyclerView);
    }

    //OVERVIEW --> ON ACTIVITY RESULT

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.ADD_REQUEST_CODE: {
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    TaskInformation newTaskInformation = data.getParcelableExtra(Constants.OVERVIEW_INFO_KEY);
                    TaskMemory.add_dateOutOfBounds(newTaskInformation.getDate());
                    addMemory(newTaskInformation);
                }
                break;
            }
            case Constants.EDIT_REQUEST_CODE: {
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    TaskInformation oldTaskInformation = TaskMemory.getTASK_LIST().get(currentTaskPosition);
                    TaskInformation newTaskInformation = data.getParcelableExtra(Constants.OVERVIEW_INFO_KEY);
                    //CHECK IF DATE CHANGED
                    Boolean dateChanged = !oldTaskInformation.getDateText().equals(newTaskInformation.getDateText());
                    if (dateChanged) {
                        TaskMemory.add_dateOutOfBounds(newTaskInformation.getDate());
                        addMemory(newTaskInformation);
                        TaskMemory.delete_taskFromMemory(oldTaskInformation);
                    } else {
                        Boolean taskInformationChanged = !TaskInformation.compareObjects(oldTaskInformation, newTaskInformation);
                        if (taskInformationChanged) {
                            addMemory(newTaskInformation);
                        }
                    }
                }
                break;
            }
        }
    }

    private void addMemory(TaskInformation newTaskInformation) {
        TaskMemory.add_taskToMemory(newTaskInformation);
        updateRecyclerViewAdapter(TaskMemory.getTASK_LIST());
        displayDate = TaskMemory.changeDisplayDate(newTaskInformation.getDate(),0);
    }


}


