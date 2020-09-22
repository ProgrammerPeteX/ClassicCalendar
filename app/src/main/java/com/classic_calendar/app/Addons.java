package com.classic_calendar.app;

import android.app.Activity;

import java.util.ArrayList;

public class Addons {
    //CONSTRUCTOR
    public Addons(Activity mainActivity, Task_RecyclerViewAdapter task_recyclerViewAdapter) {
        this.mainActivity = mainActivity;
        this.task_RecyclerViewAdapter = task_recyclerViewAdapter;
    }

    //PARAMS
    private Task_RecyclerViewAdapter task_RecyclerViewAdapter;
    private Activity mainActivity;
    private ArrayList<Addons_Interface> addons = new ArrayList<>(10);


    //METHODS
    public void addAllAddons() { //ADD NEW ADDONS HERE
        addons.add(new TimeColourCoding_Addon(mainActivity,task_RecyclerViewAdapter));
    }


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
}
