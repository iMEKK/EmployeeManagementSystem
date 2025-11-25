package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/me")
    public ResponseEntity<Employee> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // This assumes your EmployeeService has a method to find by username.
        // Let's add it if it's not there.
        Employee employee = employeeService.getEmployeeByUsername(username);
        return ResponseEntity.ok(employee);
    }
}
