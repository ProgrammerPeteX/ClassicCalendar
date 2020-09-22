package com.classic_calendar.app;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.classic_calendar.app.databinding.ActivityMainBinding;
import com.classic_calendar.app.databinding.ActivityOverviewBinding;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements Main_Interface {
    public Context mainContext;
    public Activity mainActivity;
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
        mainActivity = this;
        mainContext = this;
        getPermissions(mainContext,mainActivity);
        setContentView(viewRoot);

        TaskInformation taskInformation = new TaskInformation();
        taskInformation.setBinding(overviewBinding);
        TaskMemory.setBINDING(binding);

        //MAIN
        currentDate = LocalDate.now();
        displayDate = currentDate;

        // CREATE GUI
        create_taskGui();
        set_taskGuiPosition(TaskMemory.getTASK_LIST());

        // SET MEMORY
        Path path = Paths.get(mainContext.getFilesDir() + "/" + TaskMemory.MEMORY_FILENAME);
        Boolean memoryFileExists = Files.exists(path);
        if (memoryFileExists) {
            TaskMemory.load_memoryFromFile(mainContext);
        }
        if (TaskMemory.getMEMORY().isEmpty()) {
            TaskMemory.setDisplayDate(displayDate);
        } else {
            int addDay = 0;
            TaskMemory.setDateDisplay(currentDate, addDay);
        }

        currentTaskPosition = TaskMemory.getMEMORY_INDEX(currentDate);
        TaskMemory.setTASK_LIST(TaskMemory.getMEMORY().get(currentTaskPosition));
        updateRecyclerViewAdapter(TaskMemory.getTASK_LIST());

        //SET BUTTONS
        set_PreviousButton_OnClickListener();
        set_NextButton_OnClickListener();
        add_taskListener();
        delete_task();
        edit_task();


        //ADDONS
        run_timeColourCoding_Addon();

    }

    public void getPermissions(Context mainContext, Activity mainActivity) {
        if (ContextCompat.checkSelfPermission(mainContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(mainContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
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
                intent.putExtra(Constants.OVERVIEW_INFO_KEY, (Parcelable) taskInformation);
                startActivityForResult(intent, request_Code);
            }

        });
    }

    @Override
    public void set_taskGuiPosition(ArrayList<TaskInformation> taskInformation_List) {
        //SORT LIST
        taskInformation_List = TaskMemory.sort_taskList(taskInformation_List);
        //UPDATE LIST
        updateRecyclerViewAdapter(taskInformation_List);
    }

    public void updateRecyclerViewAdapter(List<TaskInformation> taskInformation_List) {
        TaskMemory.getRECYCLERVIEW_LIST().clear();
        task_RecyclerViewAdapter.notifyDataSetChanged();
        TaskMemory.getRECYCLERVIEW_LIST().addAll(taskInformation_List);
        task_RecyclerViewAdapter.notifyItemRangeInserted(0, TaskMemory.getRECYCLERVIEW_LIST().size());
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
                intent.putExtra(Constants.OVERVIEW_INFO_KEY, (Parcelable) taskInformation);
                intent.putExtra(Constants.ADD_OR_EDIT_KEY, Constants.EDIT);
                startActivityForResult(intent, request_Code);
            }
        });
    }

    @Override
    public void delete_task() {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
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
                task_RecyclerViewAdapter.notifyItemRangeInserted(positionStart, TaskMemory.getRECYCLERVIEW_LIST().size());
                TaskMemory.save_memoryToFile(mainContext);
                onDeleteTask();
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
                    assert newTaskInformation != null;
                    updateTask(newTaskInformation, null);
                    onAddTask();
                }
                break;
            }
            case Constants.EDIT_REQUEST_CODE: {
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    TaskInformation oldTaskInformation = TaskMemory.getTASK_LIST().get(currentTaskPosition);
                    TaskInformation newTaskInformation = data.getParcelableExtra(Constants.OVERVIEW_INFO_KEY);
                    //CHECK IF DATE CHANGED
                    assert newTaskInformation != null;
                    boolean dateChanged = !oldTaskInformation.getDateText().equals(newTaskInformation.getDateText());
                    if (dateChanged) {
                        updateTask(newTaskInformation, oldTaskInformation);
                    } else {
                        boolean taskInformationChanged = !TaskInformation.compareObjects(oldTaskInformation, newTaskInformation);
                        if (taskInformationChanged) {
                            updateTask(newTaskInformation, oldTaskInformation);
                        }
                    }
                    onEditTask();
                }
                break;
            }
        }
    }

    private void updateTask(TaskInformation newTaskInformation, TaskInformation oldTaskInformation) {
        TaskMemory.add_dateOutOfBounds(newTaskInformation.getDate());
        TaskMemory.add_taskToMemory(newTaskInformation);
        TaskMemory.delete_taskFromMemory(oldTaskInformation);
        displayDate = TaskMemory.changeDisplayDate(newTaskInformation.getDate(), 0);
        updateRecyclerViewAdapter(TaskMemory.getTASK_LIST());
        TaskMemory.save_memoryToFile(mainContext);
    }

    // ADDON TESTING SPACE

    final Runnable set_timeColourCoding_addon_runnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < TaskMemory.getTASK_LIST().size(); i ++) {
                TaskInformation task = TaskMemory.getTASK_LIST().get(i);
                if (task_RecyclerViewAdapter.getItemCount() != 0) {
                    LocalDateTime currentTime = LocalDateTime.now();
                    LocalDateTime taskStartTime = task.getDate().atTime(LocalTime.parse(task.getStartTime()));
                    LocalDateTime taskEndTime = task.getDate().atTime(LocalTime.parse(task.getEndTime()));

                    if (currentTime.isAfter(taskEndTime)) { // PAST TASKS
                        task.setTimeColourCoding(Color.valueOf((float) 0.2,(float) 0.3,(float) 0.99,(float) 0.10).toArgb());
                    } else if (currentTime.isBefore(taskStartTime)) { // FUTURE TASKS
                        task.setTimeColourCoding(Color.valueOf((float) 0.2,(float) 0.3,(float) 0.99,(float) 0.30).toArgb());
                    } else { // CURRENT TASKS
                        task.setTimeColourCoding(Color.valueOf((float) 0.2,(float) 0.3,(float) 0.99,(float) 0.20).toArgb());
                    }
                    updateRecyclerViewItem(TaskMemory.getTASK_LIST(),i);
                }
            }
        }
    };
    public void run_timeColourCoding_Addon() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mainActivity.runOnUiThread(set_timeColourCoding_addon_runnable);
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask,0,1000*60);
    }

    public void updateRecyclerViewItem(List<TaskInformation> taskInformation_List, int itemNumber) {
        TaskMemory.getRECYCLERVIEW_LIST().clear();
        TaskMemory.getRECYCLERVIEW_LIST().addAll(taskInformation_List);
        task_RecyclerViewAdapter.notifyItemChanged(itemNumber);
    }

    public void onAddTask() {
        mainActivity.runOnUiThread(set_timeColourCoding_addon_runnable);
    }

    public void onEditTask() {
        mainActivity.runOnUiThread(set_timeColourCoding_addon_runnable);
    }

    public void onDeleteTask() {

    }

}


