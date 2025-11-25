package com.example.demo.controller;

import com.example.demo.model.Timesheet;
import com.example.demo.service.TimesheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/timesheets")
public class TimesheetController {

    @Autowired
    private TimesheetService timesheetService;

    @PostMapping("/check-in")
    public ResponseEntity<Timesheet> checkIn() {
        return ResponseEntity.ok(timesheetService.checkIn());
    }

    @PostMapping("/check-out")
    public ResponseEntity<Timesheet> checkOut() {
        return ResponseEntity.ok(timesheetService.checkOut());
    }

    @GetMapping
    public ResponseEntity<List<Timesheet>> getMyTimesheets() {
        return ResponseEntity.ok(timesheetService.getMyTimesheets());
    }
}
