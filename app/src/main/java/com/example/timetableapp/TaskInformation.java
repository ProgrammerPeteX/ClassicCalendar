package com.example.timetableapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.timetableapp.databinding.ActivityOverviewBinding;

import java.time.LocalDate;

public class TaskInformation implements TaskInformation_interface, Parcelable {

    private static ActivityOverviewBinding binding;
    private static LocalDate earliestDate;
    private static LocalDate latestDate;

    //DATA
    private String taskName = "";
    private String startTime = "";
    private String endTime = "";
    private String date = "";
    private String details = "";


    public TaskInformation() {    }

    //METHODS
    public void setTaskInformation() {
        binding.taskNameEditText.setText(taskName);
        binding.startTimeEditText.setText(startTime);
        binding.endTimeEditText.setText(endTime);
        binding.dateEditText.setText(date);
        binding.detailsEditText.setText(details);
    }
    public void retrieveTaskInformation() {
        taskName = binding.taskNameEditText.getText().toString();
        startTime = binding.startTimeEditText.getText().toString();
        endTime = binding.endTimeEditText.getText().toString();
        date = binding.dateEditText.getText().toString();
        details = binding.detailsEditText.getText().toString();
    }

    //SETTERS

    public static void setEarliestDate(LocalDate earliestDate) {
            TaskInformation.earliestDate = earliestDate;
    }

    public static void setLatestDate(LocalDate latestDate) {
        TaskInformation.latestDate = latestDate;
    }

    public void setBinding(ActivityOverviewBinding binding) {
        TaskInformation.binding = binding;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public void setDetails(String details) {
        this.details = details;
    }

    //GETTERS
    public static LocalDate getEarliestDate() {
        return earliestDate;
    }

    public static LocalDate getLatestDate() {
        return latestDate;
    }

    @Override
    public String getTaskName() {
        return taskName;
    }

    @Override
    public String getStartTime() {
        return startTime;
    }

    @Override
    public String getEndTime() {
        return endTime;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public String getDetails() {
        return details;
    }

    //OTHER METHODS
    private int getHour(String time) {
        int colonPos = time.indexOf(':');
        return Integer.parseInt(time.substring(0,colonPos));
    }

    private int getMinute(String time) {
        int colonPos = time.indexOf(':');
        return Integer.parseInt(time.substring(colonPos + 1));
    }

    public int getStartHour() {
        return getHour(startTime);
    }

    public int getStartMinute() {
        return getMinute(startTime);
    }

    public int getStartTimeInMinutes() {
        return getStartHour() * 60 + getStartMinute();
    }

    public int getEndHour() {
        return getHour(endTime);
    }

    public int getEndMinute() {
        return getMinute(endTime);
    }

    public int getEndTimeInMinutes() {
        return getEndHour()*60 + getEndMinute();
    }

    public void clearOverview() {
        binding.taskNameEditText.setText("");
        binding.startTimeEditText.setText("");
        binding.endTimeEditText.setText("");
        binding.dateEditText.setText("");
        binding.detailsEditText.setText("");
    }

// PARCELABLE --------------------------------------------------------------------------------------

    protected TaskInformation(Parcel in) {
        taskName = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        date = in.readString();
        details = in.readString();
    }

    public static final Creator<TaskInformation> CREATOR = new Creator<TaskInformation>() {
        @Override
        public TaskInformation createFromParcel(Parcel in) {
            return new TaskInformation(in);
        }

        @Override
        public TaskInformation[] newArray(int size) {
            return new TaskInformation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(taskName);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(date);
        dest.writeString(details);
    }
}
