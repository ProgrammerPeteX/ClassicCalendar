package com.example.timetableapp;

public interface EditTask_interface {
    void cancel_listener();
    void accept_listener();
    void input_information();
    void set_taskGuiPosition();
    void check_ifPositionChanged();
    void find_day_inMemory();
    void find_taskPosition_inDay();
    void set_taskPosition();
    void save_taskInformation();

}
