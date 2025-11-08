package com.webserver.evrentalsystem.controller.admin;

import com.webserver.evrentalsystem.model.dto.response.RevenueResponse;
import com.webserver.evrentalsystem.service.admin.AdminDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/admin/dashboard")
@Tag(name = "4.09. Admin Dashboard", description = "API thống kê doanh thu và dữ liệu tổng quan cho Admin")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @Operation(
            summary = "Thống kê doanh thu tổng và theo trạm",
            description = "API cho phép Admin xem tổng doanh thu và doanh thu từng trạm trong khoảng thời gian chỉ định.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thống kê thành công",
                            content = @Content(schema = @Schema(implementation = RevenueResponse.class)))
            }
    )
    @GetMapping("/revenue")
    public RevenueResponse getRevenue(
            @Parameter(description = "Ngày bắt đầu", example = "2025-10-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Ngày kết thúc", example = "2025-11-04")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "ID trạm (tùy chọn)") @RequestParam(required = false) Long stationId
    ) {
        return adminDashboardService.getRevenue(startDate, endDate, stationId);
    }
}
