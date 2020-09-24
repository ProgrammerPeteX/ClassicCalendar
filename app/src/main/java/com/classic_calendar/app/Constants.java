package com.classic_calendar.app;

import android.app.Application;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Constants extends Application {
    public static final int CANCEL_REQUEST_CODE = 0;
    public static final int ADD_REQUEST_CODE = 1;
    public static final int EDIT_REQUEST_CODE = 2;
    public static final String OVERVIEW_INFO_KEY = "info";
    public static final String ADD_OR_EDIT_KEY = "add or edit?";
    public static final String ADD = "add";
    public static final String EDIT = "edit";
    public static final String TASK_NOTIFICATION_CHANNEL_ID = "task notification channel";
    public static final DateTimeFormatter DMY_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter HM_DMY_FORMATTER = DateTimeFormatter.ofPattern("HH:mm ccc dd.MM.yyyy");

    public static final int TEST_ALARM = 123123;

    Constants() {
    }

}

