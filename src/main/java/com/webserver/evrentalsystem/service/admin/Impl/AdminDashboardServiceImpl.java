package com.webserver.evrentalsystem.service.admin.impl;

import com.webserver.evrentalsystem.model.dto.response.RevenueResponse;
import com.webserver.evrentalsystem.repository.RentalRepository;
import com.webserver.evrentalsystem.service.admin.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final RentalRepository rentalRepository;

    @Override
    public RevenueResponse getRevenue(LocalDate startDate, LocalDate endDate, Long stationId) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        BigDecimal totalRevenue = rentalRepository.findTotalRevenue(startDateTime, endDateTime, stationId);
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;

        List<Object[]> results = rentalRepository.findRevenueByStation(startDateTime, endDateTime, stationId);
        Map<String, BigDecimal> revenueByStation = new HashMap<>();

        for (Object[] row : results) {
            String stationName = (String) row[0];
            BigDecimal revenue = (BigDecimal) row[1];
            revenueByStation.put(stationName, revenue);
        }

        return new RevenueResponse(totalRevenue, revenueByStation);
    }
}
