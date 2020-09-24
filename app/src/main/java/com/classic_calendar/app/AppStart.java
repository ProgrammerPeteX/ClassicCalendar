package com.classic_calendar.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class AppStart extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel taskNotification_Channel = new NotificationChannel(
                    Constants.TASK_NOTIFICATION_CHANNEL_ID,
                    "Task notification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            taskNotification_Channel.setDescription("This is the task channel");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(taskNotification_Channel);
        }
    }
}
