package com.webserver.evrentalsystem.service.admin;

import com.webserver.evrentalsystem.model.dto.response.RevenueResponse;

import java.time.LocalDate;

public interface AdminDashboardService {
    RevenueResponse getRevenue(LocalDate startDate, LocalDate endDate, Long stationId);
}
