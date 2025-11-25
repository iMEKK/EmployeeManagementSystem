package com.example.demo.service;

import com.example.demo.model.Payroll;
import java.time.YearMonth;
import java.util.List;

public interface PayrollService {
    List<Payroll> calculatePayrollForMonth(YearMonth yearMonth);
    List<Payroll> getMyPayrolls();
}
