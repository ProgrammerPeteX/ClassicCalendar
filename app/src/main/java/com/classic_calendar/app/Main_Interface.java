package com.classic_calendar.app;

import java.util.ArrayList;

public interface Main_Interface {
    void add_taskListener();

    void create_taskGui();

    void set_taskGuiPosition(ArrayList<TaskInformation> taskInformation_List);

    void delete_task();

    void edit_task();
}
