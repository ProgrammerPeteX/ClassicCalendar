package com.classic_calendar.app;

public interface TaskInformation_interface {

    void setTaskName(String taskName);

    void setStartTime(String startTime);

    void setEndTime(String endTime);

    void setDateText(String date);

    void setDetails(String details);

    String getTaskName();

    String getStartTime();

    String getEndTime();

    String getDateText();

    String getDetails();

}
