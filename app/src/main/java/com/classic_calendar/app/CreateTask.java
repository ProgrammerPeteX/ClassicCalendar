//package com.example.timetableapp;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Build;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.recyclerview.widget.ItemTouchHelper;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.timetableapp.databinding.ActivityMainBinding;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//
//public class CreateTask implements CreateTask_interface{
//
//    private Context context;
//    private ActivityMainBinding binding;
//    private TaskInformation taskInformation;
//    private ArrayList<TaskInformation> taskInformation_List;
//
//
//    ArrayList<TaskInformation> recyclerView_information = new ArrayList<>();
//
//    private Task_RecyclerViewAdapter task_RecyclerViewAdapter;
//    // CONSTRUCTOR
//    CreateTask(Context context, ActivityMainBinding binding, ArrayList<TaskInformation> taskInformation_List) {
//        this.context = context;
//        this.binding = binding;
//        this.taskInformation_List = taskInformation_List;
//    }
//
//    // METHODS
//    @Override
//    public void create_taskGui() {
//
//
//
//        recyclerView_information.addAll(taskInformation_List);
//
//        task_RecyclerViewAdapter = new Task_RecyclerViewAdapter(context, recyclerView_information);
//        binding.taskRecyclerView.setAdapter(task_RecyclerViewAdapter);
//        binding.taskRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//
//
//    }
//
//    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//    @Override
//    public void add_listener() {
//        binding.addTaskButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                taskInformation = new TaskInformation(binding);
//                //SET VISIBILITY
//                binding.mainConstraintLayout.setVisibility(View.GONE);
//                binding.overviewConstraintLayout.setVisibility(View.VISIBLE);
//            }
//        });
//    }
//
//
//    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//    @Override
//    public void accept_listener() {
//        binding.acceptButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //GET TASK INFO
//                taskInformation.retrieveTaskInformation();
//
//                taskInformation_List.add(taskInformation);
//
//                //SET TASK POSITION
//                set_taskGuiPosition(taskInformation_List);
//
//                //SET TASK LISTENER
//                set_taskListener();
//
//                //SET VISIBILITY
//                binding.overviewConstraintLayout.setVisibility(View.GONE);
//                binding.mainConstraintLayout.setVisibility(View.VISIBLE);
//
//                //SAVE
//                save_taskInformation();
//            }
//        });
//    }
//
//    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//
//    @Override
//    public void set_taskGuiPosition(List<TaskInformation> taskInformation_List) {
//
//        //SORT LIST
//        Comparator<TaskInformation> taskInformation_sortByStartTime_Comparator = new Comparator<TaskInformation>() {
//            @Override
//            public int compare(TaskInformation o1, TaskInformation o2) {
//                if (timeString_toInt(o1.getStartTime()) > timeString_toInt(o2.getStartTime())) {
//                    return 1;
//                } else {
//                    return -1;
//                }
//            }
//
//            private int timeString_toInt(String input) {
//                String militaryTime = String.format("%s%s", input.substring(0,2), input.substring(3,5));
//                int intValue = Integer.parseInt(militaryTime);
//                return intValue;
//            }
//        };
//
//        Collections.sort(taskInformation_List, taskInformation_sortByStartTime_Comparator);
//
//        //UPDATE LIST
//        recyclerView_information.clear();
//        task_RecyclerViewAdapter.notifyDataSetChanged();
//        recyclerView_information.addAll(taskInformation_List);
//        task_RecyclerViewAdapter.notifyItemRangeInserted(0,recyclerView_information.size());
//    }
//
//    @Override
//    public void set_taskListener() {
//
//    }
//
//    @Override
//    public void save_taskInformation() {
//
//    }
//
//    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//
//    @Override
//    public void cancel_listener() {
//        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //SET VISIBILITY
//                binding.overviewConstraintLayout.setVisibility(View.GONE);
//                binding.mainConstraintLayout.setVisibility(View.VISIBLE);
//            }
//        });
//    }
//
//    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//    @Override
//    public void delete_task() {
//        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                //REMOVE ITEM
//                //taskInformation.retrieveTaskInformation();;
//                taskInformation_List.remove(viewHolder.getAdapterPosition());
//
//                recyclerView_information.clear();
//                task_RecyclerViewAdapter.notifyDataSetChanged();
//                recyclerView_information.addAll(taskInformation_List);
//                task_RecyclerViewAdapter.notifyItemRangeInserted(0,recyclerView_information.size());
//
//            }
//        };
//        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.taskRecyclerView);
//    }
//
//
//    // SETTERS
//
//    // GETTERS
//}
