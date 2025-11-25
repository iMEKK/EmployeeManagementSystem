package com.example.demo.service;

import com.example.demo.model.Timesheet;
import java.util.List;

public interface TimesheetService {
    Timesheet checkIn();
    Timesheet checkOut();
    List<Timesheet> getMyTimesheets();
}
