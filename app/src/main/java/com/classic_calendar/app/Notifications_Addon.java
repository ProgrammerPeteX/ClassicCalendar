package com.classic_calendar.app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Notifications_Addon implements Addons_Interface {
    private Activity mainActivity;
    private NotificationManager notificationManager;
    private int notifyID = 1;
    private Boolean notificationIsActive = false;
    private TaskInformation taskInformation;

    Notifications_Addon(Activity mainActivity, TaskInformation taskInformation) {
        this.mainActivity = mainActivity;
        this.taskInformation = taskInformation;
    }

    public static class AlertReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Notifications_Addon.createNotification(context);
        }
    }

    private void showNotification() {
        String taskName = Addons.AddonsInfo.getNewTaskInfo().getTaskName();
        String taskDetails = Addons.AddonsInfo.getNewTaskInfo().getDetails();

        Notification.Builder notificationBuilder = new Notification.Builder(mainActivity, Constants.TASK_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_bell)
                .setContentTitle(taskName)
                .setContentText(taskDetails);

        Intent overviewIntent = new Intent(mainActivity, OverviewActivity.class);
        overviewIntent.putExtra(Constants.ADD_OR_EDIT_KEY, Constants.EDIT);
        overviewIntent.putExtra(Constants.OVERVIEW_INFO_KEY, (Parcelable) Addons.AddonsInfo.getNewTaskInfo());

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(mainActivity);
        taskStackBuilder.addParentStack(OverviewActivity.class);
        taskStackBuilder.addNextIntent(overviewIntent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyID, notificationBuilder.build());
        notificationIsActive = true;
    }

    private void cancelNotification() {
        if(!notificationIsActive) {
            //notificationManager.cancel(notifyID);
        }
    }

    private ZonedDateTime getAlarmTime() {

        taskInformation = Addons.AddonsInfo.getNewTaskInfo();

        LocalDateTime localDateTime = taskInformation.getDate().atTime(taskInformation.getStartHour(), taskInformation.getStartMinute());
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime;
    }

    private void setAlarm() {
        ZonedDateTime zonedDateTime = getAlarmTime();
        long timeInMillis = zonedDateTime.toEpochSecond()*1000;
        long beforeAlarm = -0*1000;
        long alarmTime = timeInMillis + beforeAlarm;

        Intent alertIntent = new Intent(mainActivity, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mainActivity,Constants.TEST_ALARM, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);

        String displayMessage = zonedDateTime.format(Constants.HM_DMY_FORMATTER);

        Toast.makeText(mainActivity, "Alarm set for " + displayMessage, Toast.LENGTH_LONG).show();
    }

    public static void createNotification(Context context) {
        String taskName = Addons.AddonsInfo.getNewTaskInfo().getTaskName();
        String details = Addons.AddonsInfo.getNewTaskInfo().getDetails();

        PendingIntent notificationIntent = PendingIntent.getActivity(context,0,
                new Intent(context,MainActivity.class),0);
        Notification.Builder builder = new Notification.Builder(context,Constants.TASK_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_bell)
                .setContentTitle(taskName)
                .setContentText(details);

        builder.setContentIntent(notificationIntent);
        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.TEST_ALARM, builder.build());
    }

    private void cancelAlarm() {

    }

    @Override
    public void onCreateMain() {

    }

    @Override
    public void onAddTask() {
        //showNotification();
        setAlarm();
    }

    @Override
    public void onEditTask() {

    }

    @Override
    public void onDeleteTask() {
        cancelNotification();
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onPrevious() {

    }

    @Override
    public void onClickTask() {

    }
}
