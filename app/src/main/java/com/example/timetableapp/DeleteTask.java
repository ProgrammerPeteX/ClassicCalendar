package com.example.timetableapp;

import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.timetableapp.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class DeleteTask implements DeleteTask_interface {

    private ActivityMainBinding binding;
    ArrayList<TaskInformation> taskInformation_List;

    DeleteTask(ActivityMainBinding binding, ArrayList<TaskInformation> taskInformation_List) {
        this.binding = binding;
        this.taskInformation_List = taskInformation_List;
    }

    @Override
    public void delete_listener() {
    }

    @Override
    public void delete_taskGui() {

        //binding.taskRecyclerView.
        //// REMOVE TASK INFORMATION FROM LIST
        //TaskInformation taskInformation = new TaskInformation(binding);
        //taskInformation.retrieveTaskInformation();
        //int taskToBeDeletedID = taskInformation_List.indexOf(taskInformation);
        //taskInformation_List.remove(taskToBeDeletedID);
        //
        //
        //binding.taskRecyclerView
    }

    @Override
    public void delete_taskInformation_fromMemory() {

    }
}
