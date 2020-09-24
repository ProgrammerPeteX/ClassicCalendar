package com.classic_calendar.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import com.classic_calendar.app.databinding.ActivityOverviewBinding;
import com.google.android.material.snackbar.Snackbar;

public class OverviewActivity extends AppCompatActivity implements Overview_Interface {
    public ActivityOverviewBinding overviewBinding = null;
    public Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        overviewBinding = ActivityOverviewBinding.inflate(getLayoutInflater());
        ConstraintLayout viewRoot = overviewBinding.getRoot();
        setContentView(viewRoot);
        TaskInformation taskInformation = new TaskInformation();
        taskInformation.setBinding(overviewBinding);

        String action = intent.getStringExtra(Constants.ADD_OR_EDIT_KEY);
        switch (action) {
            case Constants.ADD: {
                accept_listener();
                break;
            }
            case Constants.EDIT: {
                edit_listener();
                break;
            }
        }


        cancel_listener();
    }

    @Override
    public void accept_listener() {
        TaskInformation taskInformation = intent.getParcelableExtra(Constants.OVERVIEW_INFO_KEY);
        assert taskInformation != null;
        taskInformation.setTaskInformation();
        overviewBinding.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TaskInformation taskInformation = new TaskInformation();
                taskInformation.retrieveTaskInformation();
                Boolean isTimeOK;

                Boolean mainFieldsNotEmpty = checkMainFields(taskInformation);
                if (mainFieldsNotEmpty) {
                    isTimeOK = checkTime(taskInformation);
                } else {
                    showSnackBar("Title, Start time or end time is empty");
                    return;
                }

                if (isTimeOK) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(Constants.OVERVIEW_INFO_KEY, (Parcelable) taskInformation);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    showSnackBar("Time error");
                }
            }

            private Boolean checkTime(TaskInformation taskInformation) {
                //check for more than 1 colon
                int colonPos = taskInformation.getStartTime().indexOf(':');
                if (colonPos == -1) return false;
                if (taskInformation.getStartTime().indexOf(':', colonPos + 1) != -1) return false;
                colonPos = taskInformation.getEndTime().indexOf(':');
                if (colonPos == -1) return false;
                if (taskInformation.getEndTime().indexOf(':', colonPos + 1) != -1) return false;

                //check time
                int startHour = taskInformation.getStartHour();
                int startMinute = taskInformation.getStartMinute();
                int startTime = taskInformation.getStartTimeInMinutes();

                int endHour = taskInformation.getEndHour();
                int endMinute = taskInformation.getEndMinute();
                int endTime = taskInformation.getEndTimeInMinutes();

                if ((startHour < 0) | (startHour > 23)) return false;
                if ((startMinute < 0) | (startMinute > 59)) return false;
                if ((endHour < 0) | (endHour > 23)) return false;
                if ((endMinute < 0) | (endMinute > 59)) return false;
                if (startTime > endTime) return false;
                return true;
            }

            private Boolean checkMainFields(TaskInformation taskInformation) {
                if (taskInformation.getTaskName().isEmpty()) return false;
                if (taskInformation.getStartTime().isEmpty()) return false;
                if (taskInformation.getEndTime().isEmpty()) return false;
                return true;
            }

            public void showSnackBar(String message) {
                Snackbar snackbar = Snackbar.make(overviewBinding.overviewConstraintLayout, message, Snackbar.LENGTH_SHORT);
                snackbar.show();
                View snackView = snackbar.getView();
                TextView snackbarTextView = snackView.findViewById(R.id.snackbar_text);
                snackbarTextView.setTextColor(Color.RED);
            }
        });

    }

    @Override
    public void edit_listener() {
        overviewBinding.acceptButton.setVisibility(View.GONE);
        overviewBinding.editButton.setVisibility(View.VISIBLE);
        final TaskInformation taskInformation = intent.getParcelableExtra(Constants.OVERVIEW_INFO_KEY);
        taskInformation.setTaskInformation();

        overviewBinding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskInformation.retrieveTaskInformation();

                Intent resultIntent = new Intent();
                resultIntent.putExtra(Constants.OVERVIEW_INFO_KEY, (Parcelable) taskInformation);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    public void cancel_listener() {
        overviewBinding.cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TaskInformation taskInformation = intent.getParcelableExtra(Constants.OVERVIEW_INFO_KEY);
                //Intent resultIntent = new Intent();
                //resultIntent.putExtra(Constants.OVERVIEW_INFO_KEY, (Parcelable) taskInformation);
                //setResult(Constants.CANCEL_REQUEST_CODE, resultIntent);
                finish();
            }
        });
    }
}