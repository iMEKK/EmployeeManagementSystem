package com.example.demo.repository;

import com.example.demo.model.Employee;
import com.example.demo.model.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    Optional<Timesheet> findFirstByEmployeeAndCheckOutTimeIsNullOrderByCheckInTimeDesc(Employee employee);
    List<Timesheet> findByEmployeeOrderByCheckInTimeDesc(Employee employee);
    List<Timesheet> findByEmployeeAndCheckInTimeBetween(Employee employee, LocalDateTime start, LocalDateTime end);
}
