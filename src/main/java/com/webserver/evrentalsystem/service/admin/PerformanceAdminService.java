package com.webserver.evrentalsystem.service.admin;

import com.webserver.evrentalsystem.model.dto.response.StaffPerformanceResponse;

import java.util.List;

public interface PerformanceAdminService {
    List<StaffPerformanceResponse> getAllPerformance();
}
