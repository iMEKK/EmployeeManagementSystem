package com.example.demo.repository;

import com.example.demo.model.Employee;
import com.example.demo.model.LeaveRequest;
import com.example.demo.model.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployee(Employee employee);
    List<LeaveRequest> findByEmployeeAndStatusAndStartDateBetween(Employee employee, LeaveStatus status, LocalDate start, LocalDate end);
}
