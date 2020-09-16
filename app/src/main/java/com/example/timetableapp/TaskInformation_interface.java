package com.example.timetableapp;

public interface TaskInformation_interface {

    void setTaskName(String taskName);
    void setStartTime(String startTime);
    void setEndTime(String endTime);
    void setDate(String date);
    void setDetails(String details);

    String getTaskName();
    String getStartTime();
    String getEndTime();
    String getDate();
    String getDetails();

}
