package com.classic_calendar.app;

import android.os.Parcel;
import android.os.Parcelable;

import com.classic_calendar.app.databinding.ActivityOverviewBinding;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;

public class TaskInformation implements TaskInformation_interface, Parcelable, Serializable {

    private static ActivityOverviewBinding binding;


    //DATA
    private String taskName = "";
    private String startTime = "";
    private String endTime = "";
    private String dateText = "";
    private String details = "";

    private LocalDate date;


    public TaskInformation() {
    }

    //METHODS
    public void setTaskInformation() {
        binding.taskNameEditText.setText(taskName);
        binding.startTimeEditText.setText(startTime);
        binding.endTimeEditText.setText(endTime);
        binding.dateEditText.setText(dateText);
        binding.detailsEditText.setText(details);
    }

    public void retrieveTaskInformation() {
        taskName = binding.taskNameEditText.getText().toString();
        startTime = binding.startTimeEditText.getText().toString();
        endTime = binding.endTimeEditText.getText().toString();
        dateText = binding.dateEditText.getText().toString();
        details = binding.detailsEditText.getText().toString();
    }

    //SETTERS
    public void setDate(LocalDate date) {
        this.date = date;
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
    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    @Override
    public void setDetails(String details) {
        this.details = details;
    }

    //GETTERS
    public LocalDate getDate() {
        this.date = LocalDate.parse(dateText, Constants.FORMATTER);
        return date;
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
    public String getDateText() {
        return dateText;
    }

    @Override
    public String getDetails() {
        return details;
    }

    //OTHER METHODS
    private int getHour(String time) {
        int colonPos = time.indexOf(':');
        return Integer.parseInt(time.substring(0, colonPos));
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
        return getEndHour() * 60 + getEndMinute();
    }

    public void clearOverview() {
        binding.taskNameEditText.setText("");
        binding.startTimeEditText.setText("");
        binding.endTimeEditText.setText("");
        binding.dateEditText.setText("");
        binding.detailsEditText.setText("");
    }

//STATIC METHODS

    public static Boolean compareObjects(TaskInformation a, TaskInformation b) {
        int comparison = Comparator.comparing(TaskInformation::getTaskName)
                .thenComparing(TaskInformation::getStartTime)
                .thenComparing(TaskInformation::getEndTime)
                .thenComparing(TaskInformation::getDateText)
                .thenComparing(TaskInformation::getDetails)
                .compare(a, b);
        if (comparison == 0) {
            return true;
        } else {
            return false;
        }
    }


// PARCELABLE --------------------------------------------------------------------------------------

    protected TaskInformation(Parcel in) {
        taskName = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        dateText = in.readString();
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
        dest.writeString(dateText);
        dest.writeString(details);
    }
}
