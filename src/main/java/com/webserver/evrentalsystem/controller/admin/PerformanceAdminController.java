package com.webserver.evrentalsystem.controller.admin;

import com.webserver.evrentalsystem.model.dto.response.StaffPerformanceResponse;
import com.webserver.evrentalsystem.service.admin.PerformanceAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/admin/performance")
@Tag(name = "4.8. Admin Performance", description = "API quản lý hiệu suất của admin")
@SecurityRequirement(name = "bearerAuth")
public class PerformanceAdminController {

    @Autowired
    private PerformanceAdminService performanceAdminService;

    @Operation(summary = "Lấy hiệu suất của tất cả nhân viên", description = "Trả về danh sách gồm lượt giao, nhận, đánh giá trung bình, và số khiếu nại của từng nhân viên.")
    @GetMapping
    public List<StaffPerformanceResponse> getPerformance() {
        return performanceAdminService.getAllPerformance();
    }
}
