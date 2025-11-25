package com.example.demo.service;

import com.example.demo.model.Employee;
import com.example.demo.model.Timesheet;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimesheetServiceImpl implements TimesheetService {

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Timesheet checkIn() {
        Employee employee = getCurrentEmployee();
        Timesheet timesheet = new Timesheet();
        timesheet.setEmployee(employee);
        timesheet.setCheckInTime(LocalDateTime.now());
        return timesheetRepository.save(timesheet);
    }

    @Override
    public Timesheet checkOut() {
        Employee employee = getCurrentEmployee();
        // Find the last check-in without a check-out
        Timesheet timesheet = timesheetRepository.findFirstByEmployeeAndCheckOutTimeIsNullOrderByCheckInTimeDesc(employee)
                .orElseThrow(() -> new IllegalStateException("No active check-in found for this employee."));
        
        timesheet.setCheckOutTime(LocalDateTime.now());
        return timesheetRepository.save(timesheet);
    }

    @Override
    public List<Timesheet> getMyTimesheets() {
        Employee employee = getCurrentEmployee();
        return timesheetRepository.findByEmployeeOrderByCheckInTimeDesc(employee);
    }

    private Employee getCurrentEmployee() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found with username: " + username));
    }
}
