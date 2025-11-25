package com.example.demo.service;

import com.example.demo.model.LeaveRequest;
import java.util.List;

public interface LeaveRequestService {
    // Employee methods
    LeaveRequest createLeaveRequest(LeaveRequest leaveRequest);
    List<LeaveRequest> getMyLeaveRequests();

    // Admin methods
    List<LeaveRequest> getAllLeaveRequests();
    LeaveRequest approveLeaveRequest(Long id);
    LeaveRequest rejectLeaveRequest(Long id);
}
