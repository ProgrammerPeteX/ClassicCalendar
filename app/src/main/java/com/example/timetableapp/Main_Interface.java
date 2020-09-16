package com.example.timetableapp;

        import java.util.ArrayList;
        import java.util.List;

public interface Main_Interface {
    void add_taskListener();
    void create_taskGui();
    void set_taskGuiPosition(List<TaskInformation> taskInformation_List);
    void delete_task();
    void edit_task();
}
