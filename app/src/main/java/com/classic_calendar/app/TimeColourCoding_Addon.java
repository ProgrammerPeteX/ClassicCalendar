package com.classic_calendar.app;

import android.app.Activity;
import android.graphics.Color;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

public class TimeColourCoding_Addon implements Addons_Interface{

    private Task_RecyclerViewAdapter task_RecyclerViewAdapter;
    private Activity activity;

    TimeColourCoding_Addon(Activity mainActivity, Task_RecyclerViewAdapter task_RecyclerViewAdapter) {
        this.activity = mainActivity;
        this.task_RecyclerViewAdapter = task_RecyclerViewAdapter;
    }

    private final Runnable set_timeColourCoding_addon_runnable = new Runnable() {
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
                    Task_RecyclerViewAdapter.updateRecyclerViewItem(task_RecyclerViewAdapter, TaskMemory.getTASK_LIST(),i);
                }
            }
        }
    };

    public void runOnce() {
        activity.runOnUiThread(set_timeColourCoding_addon_runnable);
    }
    public void runTimer() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnce();
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask,0,1000*60);
    }

    @Override
    public void onCreateMain() {
        runTimer();
    }

    @Override
    public void onAddTask() {
        runOnce();
    }

    @Override
    public void onEditTask() {
        runOnce();
    }

    @Override
    public void onDeleteTask() {

    }

    @Override
    public void onNext() {

    }

    @Override
    public void onPrevious() {

    }
}
