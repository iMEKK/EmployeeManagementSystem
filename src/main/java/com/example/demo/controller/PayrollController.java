package com.example.demo.controller;

import com.example.demo.model.Payroll;
import com.example.demo.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/payrolls")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @PostMapping("/calculate")
    public ResponseEntity<List<Payroll>> calculatePayroll(@RequestParam("month") String yearMonthStr) {
        YearMonth yearMonth = YearMonth.parse(yearMonthStr); // Expects "YYYY-MM" format
        List<Payroll> payrolls = payrollService.calculatePayrollForMonth(yearMonth);
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Payroll>> getMyPayrolls() {
        List<Payroll> payrolls = payrollService.getMyPayrolls();
        return ResponseEntity.ok(payrolls);
    }
}
