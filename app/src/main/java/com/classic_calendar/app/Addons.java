package com.classic_calendar.app;

import android.app.Activity;

import java.util.ArrayList;

public class Addons {
    private TaskInformation taskInformation;

    //CONSTRUCTOR
    public Addons(Activity mainActivity, Task_RecyclerViewAdapter task_recyclerViewAdapter) {
        this.mainActivity = mainActivity;
        this.task_RecyclerViewAdapter = task_recyclerViewAdapter;
    }

    //PARAMS
    private Task_RecyclerViewAdapter task_RecyclerViewAdapter;
    private Activity mainActivity;
    private ArrayList<Addons_Interface> addons = new ArrayList<>(10);

    //ADDONS AND ADDONS INFO

    public void addAllAddons() {                                                                    //ADD NEW ADDONS HERE
        addons.add(new TimeColourCoding_Addon(mainActivity,task_RecyclerViewAdapter));
        addons.add(new Notifications_Addon(mainActivity,taskInformation));
    }

    public static class AddonsInfo {
        private static TaskInformation newTaskInfo;

        //SETTERS
        public static void setNewTaskInfo(TaskInformation newTaskInfo) {
            AddonsInfo.newTaskInfo = newTaskInfo;
        }

        //GETTERS
        public static TaskInformation getNewTaskInfo() {
            return newTaskInfo;
        }
    }

    //INTERFACE METHODS

    public void onCreateMain_addons() {
        for (Addons_Interface addon : addons) {
            addon.onCreateMain();
        }
    }

    public void onAddTask_addons() {
        for (Addons_Interface addon : addons) {
            addon.onAddTask();
        }
    }

    public void onEditTask_addons() {
        for (Addons_Interface addon : addons) {
            addon.onEditTask();
        }
    }

    public void onDeleteTask_addons() {
        for (Addons_Interface addon : addons) {
            addon.onDeleteTask();
        }
    }

    public void onPrevious_addons() {
        for (Addons_Interface addon : addons) {
            addon.onPrevious();
        }
    }

    public void onNext_addons() {
        for (Addons_Interface addon : addons) {
            addon.onNext();
        }
    }

    public void onClickTask() {
        for (Addons_Interface addon : addons) {
            addon.onClickTask();
        }
    }
}
