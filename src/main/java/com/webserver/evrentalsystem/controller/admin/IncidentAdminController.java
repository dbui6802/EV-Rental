package com.webserver.evrentalsystem.controller.admin;

import com.webserver.evrentalsystem.model.dto.entitydto.IncidentReportDto;
import com.webserver.evrentalsystem.model.dto.request.ResolveIncidentRequest;
import com.webserver.evrentalsystem.service.admin.IncidentAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/incidents")
@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "4.10. Admin Incident", description = "API cho Admin quản lý các báo cáo sự cố")
public class IncidentAdminController {

    @Autowired
    private IncidentAdminService incidentAdminService;

    @Operation(
            summary = "Admin xem danh sách báo cáo sự cố",
            description = "API để Admin lấy danh sách tất cả các báo cáo sự cố, có thể lọc theo trạng thái."
    )
    @GetMapping
    public ResponseEntity<List<IncidentReportDto>> getAllIncidents(
            @Parameter(description = "Filter by incident status (e.g., PENDING, IN_REVIEW, RESOLVED)")
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(incidentAdminService.getAllIncidents(status));
    }
}
