package com.classic_calendar.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.classic_calendar.app.databinding.ActivityMainBinding;

import java.time.format.DateTimeFormatter;

public class Constants extends Application {
    public static final int ADD_REQUEST_CODE = 1;
    public static final int EDIT_REQUEST_CODE = 0;
    public static final String OVERVIEW_INFO_KEY = "info";
    public static final String ADD_OR_EDIT_KEY = "add or edit?";
    public static final String ADD = "add";
    public static final String EDIT = "edit";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    Constants() {
    }

}

