package com.example.timetableapp;

import com.example.timetableapp.databinding.ActivityMainBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TaskMemory {
    //CONSTANTS
    private static ArrayList<ArrayList<TaskInformation>> MEMORY = new ArrayList<>(1024);
    private static ArrayList<TaskInformation> TASK_LIST = new ArrayList<>(32);
    private static ArrayList<TaskInformation> RECYCLERVIEW_LIST = new ArrayList<>(32);
    private static ActivityMainBinding BINDING;

    //VARS
    private static LocalDate earliestDate;
    private static LocalDate latestDate;

    //CONSTRUCTOR
    TaskMemory() {}

    //SETTERS
    public static void setTASK_LIST(ArrayList<TaskInformation> taskList) {
        TASK_LIST = taskList;
    }

    public static void setRECYCLERVIEW_LIST(ArrayList<TaskInformation> recyclerviewList) {
        RECYCLERVIEW_LIST = recyclerviewList;
    }

    public static void setEarliestDate(LocalDate earliestDate) {
        TaskMemory.earliestDate = earliestDate;
    }

    public static void setLatestDate(LocalDate latestDate) {
        TaskMemory.latestDate = latestDate;
    }

    public static void setMEMORY(ArrayList<ArrayList<TaskInformation>> MEMORY) {
        TaskMemory.MEMORY = MEMORY;
    }

    public static void setBINDING(ActivityMainBinding BINDING) {
        TaskMemory.BINDING = BINDING;
    }

    //GETTERS

    public static ArrayList<TaskInformation> getTASK_LIST() {
        return TASK_LIST;
    }

    public static ArrayList<TaskInformation> getRECYCLERVIEW_LIST() {
        return RECYCLERVIEW_LIST;
    }

    public static LocalDate getEarliestDate() {
        return earliestDate;
    }

    public static LocalDate getLatestDate() {
        return latestDate;
    }

    public static ArrayList<ArrayList<TaskInformation>> getMEMORY() {
        return MEMORY;
    }

    public static ActivityMainBinding getBINDING() {
        return BINDING;
    }

    //STATIC METHODS
    public static int getDateDifference(LocalDate newDate, LocalDate oldDate) {
        return newDate.compareTo(oldDate);
    }

    public static int getMEMORY_INDEX(LocalDate date) {
        return getDateDifference(date, getEarliestDate());
    }
    public static void setDisplayDate(LocalDate displayDate) {
        //CHANGE TEXT
        LocalDate newDate = formatDateDisplay(displayDate, 0);
        setEarliestDate(displayDate);
        setLatestDate(displayDate);
        if (MEMORY.isEmpty()) {
            MEMORY.add(TASK_LIST);
        }
        // LOAD DAY-RECYCLERVIEW
    }
    public static LocalDate changeDisplayDate(LocalDate currentDisplayDate, int addDay) {
        //CHANGE TEXT
        LocalDate newDate = formatDateDisplay(currentDisplayDate, addDay);

        //CHECK IF NEW DATE
        add_dateOutOfBounds(newDate);
        return newDate;
    }

    private static LocalDate formatDateDisplay(LocalDate displayDate, int addDay) {

        displayDate = displayDate.plusDays(addDay);
        String displayDateText = displayDate.getDayOfWeek().toString() + "\n" + displayDate.format(Constants.FORMATTER);
        BINDING.currentDateTextView.setText(displayDateText);
        return displayDate;
    }

    public static int add_dateOutOfBounds(LocalDate newDate)  {
        int newDayPos = newDate.compareTo(getEarliestDate());
        if (newDayPos >= (MEMORY.size() - 1)) {
            int extraPositiveElements = newDayPos - (MEMORY.size() - 1);
            for (int i = 0; i < extraPositiveElements; i++) {
                ArrayList<TaskInformation> newTaskInformation_List = new ArrayList<>(32);
                MEMORY.add(newTaskInformation_List);
            }
            setLatestDate(newDate);
        } else if (newDayPos < 0) {
            int extraNegativeElements = -newDayPos;
            for (int i = 0; i < extraNegativeElements; i++) {
                ArrayList<TaskInformation> newTaskInformation_List = new ArrayList<>(32);
                MEMORY.add(0, newTaskInformation_List);
            }
            setEarliestDate(newDate);
            newDayPos = 0;
        }
        return newDayPos;
    }

    public static void add_taskToMemory(TaskInformation newTaskInformation) {
        int datePos = getMEMORY_INDEX(newTaskInformation.getDate());
        setTASK_LIST(MEMORY.get(datePos));
        TASK_LIST.add(newTaskInformation);
        setTASK_LIST(sort_taskList(TASK_LIST));

}
    public static ArrayList<TaskInformation> sort_taskList(ArrayList<TaskInformation> taskList) {
        //SORT LIST
        Comparator<TaskInformation> taskInformation_sortByStartTime_Comparator = new Comparator<TaskInformation>() {
            @Override
            public int compare(TaskInformation o1, TaskInformation o2) {
                if (timeString_toInt(o1.getStartTime()) > timeString_toInt(o2.getStartTime())) {
                    return 1;
                } else {
                    return -1;
                }
            }
            private int timeString_toInt(String input) {
                String militaryTime = String.format("%s%s", input.substring(0,2), input.substring(3,5));
                int intValue = Integer.parseInt(militaryTime);
                return intValue;
            }
        };
        Collections.sort(taskList, taskInformation_sortByStartTime_Comparator);
        return taskList;
    }

    public static void delete_taskFromMemory(TaskInformation oldTaskInformation) {
        LocalDate oldDate = oldTaskInformation.getDate();
        int oldDayPos = getMEMORY_INDEX(oldDate);
        MEMORY.get(oldDayPos).remove(oldTaskInformation);
    }

    public static void load_memoryToFile() {

    }

    public static void save_memoryToFile() {

    }
}
