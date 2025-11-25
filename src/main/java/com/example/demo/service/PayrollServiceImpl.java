package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.LeaveRequestRepository;
import com.example.demo.repository.PayrollRepository;
import com.example.demo.repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PayrollServiceImpl implements PayrollService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private TimesheetRepository timesheetRepository;
    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    @Autowired
    private PayrollRepository payrollRepository;

    private static final int STANDARD_WORK_HOURS_PER_DAY = 8;
    private static final BigDecimal OT_RATE_PER_HOUR = new BigDecimal("100");
    private static final int MONTHLY_WORKING_DAYS = 20; // Assumption for calculating hourly rate

    @Override
    public List<Payroll> calculatePayrollForMonth(YearMonth yearMonth) {
        List<Employee> employees = employeeRepository.findAll();
        List<Payroll> payrolls = new ArrayList<>();

        LocalDate monthStartDate = yearMonth.atDay(1);
        LocalDate monthEndDate = yearMonth.atEndOfMonth();

        for (Employee employee : employees) {
            if (employee.getBaseSalary() == null || employee.getBaseSalary().compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            BigDecimal totalMonthlyWorkHours = BigDecimal.ZERO;
            BigDecimal totalOtPay = BigDecimal.ZERO;

            // Get all approved leave dates for the month
            List<LeaveRequest> leaveRequests = leaveRequestRepository.findByEmployeeAndStatusAndStartDateBetween(
                    employee, LeaveStatus.APPROVED, monthStartDate, monthEndDate);
            Map<LocalDate, Boolean> approvedLeaveDays = leaveRequests.stream()
                    .flatMap(lr -> lr.getStartDate().datesUntil(lr.getEndDate().plusDays(1)))
                    .collect(Collectors.toMap(date -> date, date -> true));

            // Get all timesheets for the month
            List<Timesheet> timesheets = timesheetRepository.findByEmployeeAndCheckInTimeBetween(
                    employee, monthStartDate.atStartOfDay(), monthEndDate.plusDays(1).atStartOfDay());
            Map<LocalDate, Duration> workDurations = timesheets.stream()
                    .filter(ts -> ts.getCheckInTime() != null && ts.getCheckOutTime() != null)
                    .collect(Collectors.groupingBy(
                            ts -> ts.getCheckInTime().toLocalDate(),
                            Collectors.reducing(Duration.ZERO,
                                    ts -> Duration.between(ts.getCheckInTime(), ts.getCheckOutTime()),
                                    Duration::plus)
                    ));

            // Iterate through each day of the month
            for (LocalDate day = monthStartDate; !day.isAfter(monthEndDate); day = day.plusDays(1)) {
                
                if (approvedLeaveDays.containsKey(day)) {
                    // Day of approved leave
                    totalMonthlyWorkHours = totalMonthlyWorkHours.add(new BigDecimal(STANDARD_WORK_HOURS_PER_DAY));
                } else if (workDurations.containsKey(day)) {
                    // Day with actual work
                    Duration dailyDuration = workDurations.get(day);
                    BigDecimal dailyHoursWorked = new BigDecimal(dailyDuration.toMinutes()).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);

                    BigDecimal standardHours = dailyHoursWorked.min(new BigDecimal(STANDARD_WORK_HOURS_PER_DAY));
                    BigDecimal overtimeHours = dailyHoursWorked.subtract(standardHours);
                    
                    if (overtimeHours.compareTo(BigDecimal.ZERO) > 0) {
                        totalOtPay = totalOtPay.add(overtimeHours.multiply(OT_RATE_PER_HOUR));
                    }
                    
                    totalMonthlyWorkHours = totalMonthlyWorkHours.add(standardHours);
                }
                // If it's a weekend or a day off with no leave, hours are zero and nothing is added.
            }

            // Calculate final pay
            BigDecimal hourlyRate = employee.getBaseSalary()
                                            .divide(new BigDecimal(MONTHLY_WORKING_DAYS * STANDARD_WORK_HOURS_PER_DAY), 2, RoundingMode.HALF_UP);
            BigDecimal totalBasePay = totalMonthlyWorkHours.multiply(hourlyRate);
            BigDecimal netPay = totalBasePay.add(totalOtPay);

            // Create and Save Payroll Record
            Payroll payroll = new Payroll();
            payroll.setEmployee(employee);
            payroll.setPayPeriodStartDate(monthStartDate);
            payroll.setPayPeriodEndDate(monthEndDate);
            payroll.setBaseSalary(totalBasePay); // Now represents calculated base pay for the month
            payroll.setOvertimePay(totalOtPay);
            payroll.setDeductions(BigDecimal.ZERO); // No deductions in this new logic
            payroll.setNetPay(netPay);
            payroll.setCalculationDate(LocalDateTime.now());
            
            payrolls.add(payrollRepository.save(payroll));
        }
        return payrolls;
    }

    @Override
    public List<Payroll> getMyPayrolls() {
        Employee employee = getCurrentEmployee();
        return payrollRepository.findByEmployee(employee);
    }

    private Employee getCurrentEmployee() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found with username: " + username));
    }
}
