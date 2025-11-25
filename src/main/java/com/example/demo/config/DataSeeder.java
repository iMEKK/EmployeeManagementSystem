package com.example.demo.config;

import com.example.demo.model.*;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.LeaveRequestRepository;
import com.example.demo.repository.PayrollRepository;
import com.example.demo.repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PayrollRepository payrollRepository;
    @Autowired
    private TimesheetRepository timesheetRepository;
    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists to prevent re-seeding
        if (employeeRepository.count() > 0) {
            return;
        }

        // 1. Admin User
        Employee admin = new Employee();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("adminpass"));
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail("admin@system.com");
        admin.setRole(Role.ROLE_ADMIN);
        admin.setBaseSalary(new BigDecimal("90000.00"));
        employeeRepository.save(admin);

        // 2. Employee 1 (Diligent, works OT)
        Employee employee1 = new Employee();
        employee1.setUsername("employee1");
        employee1.setPassword(passwordEncoder.encode("pass1"));
        employee1.setFirstName("John");
        employee1.setLastName("Doe");
        employee1.setEmail("john.doe@work.com");
        employee1.setRole(Role.ROLE_EMPLOYEE);
        employee1.setBaseSalary(new BigDecimal("60000.00"));
        employeeRepository.save(employee1);

        // Timesheets for Employee 1 (includes OT)
        createTimesheet(employee1, 3, LocalTime.of(9, 0), LocalTime.of(19, 30)); // 10.5 hours -> 2.5h OT
        createTimesheet(employee1, 2, LocalTime.of(8, 55), LocalTime.of(17, 0));  // ~8 hours
        createTimesheet(employee1, 1, LocalTime.of(9, 5), LocalTime.of(18, 5));   // 9 hours -> 1h OT

        // 3. Employee 2 (Normal, takes leave)
        Employee employee2 = new Employee();
        employee2.setUsername("employee2");
        employee2.setPassword(passwordEncoder.encode("pass2"));
        employee2.setFirstName("Jane");
        employee2.setLastName("Smith");
        employee2.setEmail("jane.smith@work.com");
        employee2.setRole(Role.ROLE_EMPLOYEE);
        employee2.setBaseSalary(new BigDecimal("55000.00"));
        employeeRepository.save(employee2);

        // Timesheets for Employee 2
        createTimesheet(employee2, 2, LocalTime.of(9, 15), LocalTime.of(17, 5)); // ~8 hours

        // Approved Leave for Employee 2
        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(employee2);
        leave.setStartDate(LocalDate.now().minusDays(5));
        leave.setEndDate(LocalDate.now().minusDays(5));
        leave.setReason("Sick Leave");
        leave.setStatus(LeaveStatus.APPROVED);
        leaveRequestRepository.save(leave);
    }

    private void createTimesheet(Employee employee, int daysAgo, LocalTime start, LocalTime end) {
        Timesheet ts = new Timesheet();
        ts.setEmployee(employee);
        ts.setCheckInTime(LocalDateTime.of(LocalDate.now().minusDays(daysAgo), start));
        ts.setCheckOutTime(LocalDateTime.of(LocalDate.now().minusDays(daysAgo), end));
        timesheetRepository.save(ts);
    }
}
