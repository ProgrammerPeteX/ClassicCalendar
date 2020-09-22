package com.classic_calendar.app;

import android.content.Context;
import android.widget.Toast;

import com.classic_calendar.app.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TaskMemory implements Serializable {
    //CONSTANTS
    private static ArrayList<ArrayList<TaskInformation>> MEMORY = new ArrayList<>(1024);
    private static ArrayList<TaskInformation> TASK_LIST = new ArrayList<>(32);
    private static ArrayList<TaskInformation> RECYCLERVIEW_LIST = new ArrayList<>(32);
    private static ActivityMainBinding BINDING;
    public static final String MEMORY_FILENAME = "MEMORY.txt";
    public static final String EARLIEST_DATE_FILENAME = "EARLIEST_DATE.txt";
    public static final String LATEST_DATE_FILENAME = "LATEST_DATE.txt";

    //VARS
    private static LocalDate earliestDate;
    private static LocalDate latestDate;

    //CONSTRUCTOR
    TaskMemory() {
    }

    //SETTERS
    public static void setEarliestDate(LocalDate earliestDate) {
        TaskMemory.earliestDate = earliestDate;
    }

    public static void setBINDING(ActivityMainBinding BINDING) {
        TaskMemory.BINDING = BINDING;
    }

    public static void setLatestDate(LocalDate latestDate) {
        TaskMemory.latestDate = latestDate;
    }

    public static void setTASK_LIST(ArrayList<TaskInformation> taskList) {
        TASK_LIST = taskList;
    }

    public static void setMEMORY(ArrayList<ArrayList<TaskInformation>> MEMORY) {
        TaskMemory.MEMORY = MEMORY;
    }

    //GETTERS

    public static LocalDate getEarliestDate() {
        return earliestDate;
    }

    public static LocalDate getLatestDate() {
        return latestDate;
    }

    public static ArrayList<TaskInformation> getTASK_LIST() {
        return TASK_LIST;
    }

    public static ArrayList<TaskInformation> getRECYCLERVIEW_LIST() {
        return RECYCLERVIEW_LIST;
    }

    public static ArrayList<ArrayList<TaskInformation>> getMEMORY() {
        return MEMORY;
    }

    //STATIC METHODS
    public static int getMEMORY_INDEX(LocalDate date) {
        return getDateDifference(date, getEarliestDate());
    }
    public static LocalDate changeDisplayDate(LocalDate currentDisplayDate, int addDay) {
        //CHANGE TEXT
        LocalDate newDate = setDateDisplay(currentDisplayDate, addDay);

        //CHECK IF NEW DATE
        add_dateOutOfBounds(newDate);
        return newDate;
    }

    public static void add_dateOutOfBounds(LocalDate newDate) {
        int newDayPos = getMEMORY_INDEX(newDate);
        boolean positiveOutOfBounds = getDateDifference(newDate, getLatestDate()) > 0;
        boolean negativeOutOfBounds = getDateDifference(newDate, getEarliestDate()) < 0;
        if (positiveOutOfBounds) {
            int extraPositiveElements = newDayPos - (MEMORY.size() - 1);
            for (int i = 0; i < extraPositiveElements; i++) {
                ArrayList<TaskInformation> newTaskInformation_List = new ArrayList<>(32);
                MEMORY.add(newTaskInformation_List);
            }
            setLatestDate(newDate);
        } else if (negativeOutOfBounds) {
            int extraNegativeElements = -newDayPos;
            for (int i = 0; i < extraNegativeElements; i++) {
                ArrayList<TaskInformation> newTaskInformation_List = new ArrayList<>(32);
                MEMORY.add(0, newTaskInformation_List);
            }
            setEarliestDate(newDate);
        }
    }

    private static int getDateDifference(LocalDate newDate, LocalDate oldDate) {
        return newDate.compareTo(oldDate);
    }

    public static void setup_displayDate(LocalDate displayDate) {
        //CHANGE TEXT
        setDateDisplay(displayDate, 0);
        setEarliestDate(displayDate);
        setLatestDate(displayDate);
        MEMORY.add(TASK_LIST);
        // LOAD DAY-RECYCLERVIEW
    }

    public static LocalDate setDateDisplay(LocalDate displayDate, int addDay) {
        displayDate = displayDate.plusDays(addDay);
        String displayDateText = displayDate.getDayOfWeek() + "\n" + dateToString(displayDate);
        BINDING.currentDateTextView.setText(displayDateText);
        return displayDate;
    }


    public static void add_taskToMemory(TaskInformation newTaskInformation) {
        int datePos = getMEMORY_INDEX(newTaskInformation.getDate());
        setTASK_LIST(MEMORY.get(datePos));
        TASK_LIST.add(newTaskInformation);
        setTASK_LIST(sort_taskList(TASK_LIST));

    }

    public static void save_memoryToFile(Context context) {
        //MAIN MEMORY
        saveObject(context, MEMORY_FILENAME, MEMORY);
        saveText(context, EARLIEST_DATE_FILENAME, dateToString(getEarliestDate()));
        saveText(context, LATEST_DATE_FILENAME, dateToString(getLatestDate()));
    }

    public static void load_memoryFromFile(Context context) {
        ArrayList<ArrayList<TaskInformation>> MEMORY = (ArrayList<ArrayList<TaskInformation>>) loadObject(context, MEMORY_FILENAME);
        setMEMORY(MEMORY);
        LocalDate earliestDate = stringToDate(loadText(context, EARLIEST_DATE_FILENAME));
        setEarliestDate(earliestDate);
        LocalDate latestDate = stringToDate(loadText(context, LATEST_DATE_FILENAME));
        setLatestDate(latestDate);

    }

    public static void delete_taskFromMemory(TaskInformation oldTaskInformation) {
        if (oldTaskInformation != null) {
            LocalDate oldDate = oldTaskInformation.getDate();
            int oldDayPos = getMEMORY_INDEX(oldDate);
            MEMORY.get(oldDayPos).remove(oldTaskInformation);
        }
    }


    private static void saveObject(Context context, String FILENAME, ArrayList<ArrayList<TaskInformation>> object) {
        File dir = context.getFileStreamPath(FILENAME);
        FileOutputStream fileName = null;
        try {
            fileName = new FileOutputStream(dir, false);
            ObjectOutputStream output = new ObjectOutputStream(fileName);
            output.writeObject(object);
            output.close();
            String fileLocation = context.getFilesDir() + "/" + FILENAME;
            Toast.makeText(context,fileLocation,Toast.LENGTH_LONG);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Object loadObject(Context context, String FILENAME) {
        File file = context.getFileStreamPath(FILENAME);
        FileInputStream is = null;
        Object object = null;
        try {
            is = new FileInputStream(file);
            ObjectInputStream input = new ObjectInputStream(is);
            object = (ArrayList<ArrayList<TaskInformation>>) input.readObject();
            input.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return object;
    }

    private static void saveText(Context context, String FILENAME, String text) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fileOutputStream.write(text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static String loadText(Context context, String FILENAME) {
        FileInputStream fileInputStream = null;
        String inputString = null;
        try {
            fileInputStream = context.openFileInput(FILENAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String text = bufferedReader.readLine();
            stringBuilder.append(text);
            inputString = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputString;
    }


    public static String dateToString(LocalDate date) {
        return date.format(Constants.FORMATTER);
    }

    public static LocalDate stringToDate(String dateText) {
        return LocalDate.parse(dateText, Constants.FORMATTER);
    }

    public static ArrayList<TaskInformation> sort_taskList(ArrayList<TaskInformation> taskList) {
        //SORT LIST
        Comparator<TaskInformation> taskInformation_sortByStartTime_Comparator = new Comparator<TaskInformation>() {
            @Override
            public int compare(TaskInformation o1, TaskInformation o2) {
                return Integer.compare(timeString_toInt(o1.getStartTime()), timeString_toInt(o2.getStartTime()));
            }

            private int timeString_toInt(String input) {
                String militaryTime = String.format("%s%s", input.substring(0, 2), input.substring(3, 5));
                return Integer.parseInt(militaryTime);
            }
        };
        Collections.sort(taskList, taskInformation_sortByStartTime_Comparator);
        return taskList;
    }
}
