package com.webserver.evrentalsystem.controller.staff;

import com.webserver.evrentalsystem.model.dto.entitydto.IncidentReportDto;
import com.webserver.evrentalsystem.model.dto.request.IncidentReportUpdateRequest;
import com.webserver.evrentalsystem.service.staff.IncidentReportStaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff/incident-reports")
@Tag(name = "3.4. Staff Incident Report", description = "API quản lý báo cáo sự cố bởi Staff")
@SecurityRequirement(name = "bearerAuth")
public class IncidentReportStaffController {

    @Autowired
    private IncidentReportStaffService incidentReportStaffService;

    @Operation(
            summary = "Staff xem danh sách báo cáo sự cố",
            description = "API để staff xem danh sách tất cả báo cáo sự cố, có thể lọc theo trạng thái."
    )
    @GetMapping
    public ResponseEntity<List<IncidentReportDto>> getAllIncidents(
            @Parameter(description = "Lọc theo trạng thái sự cố (ví dụ: PENDING, IN_REVIEW, RESOLVED)")
            @RequestParam(required = false) String status) {
        List<IncidentReportDto> incidentReports = incidentReportStaffService.getAllIncidents(status);
        return ResponseEntity.ok(incidentReports);
    }

    @Operation(
            summary = "Staff xem chi tiết một báo cáo sự cố",
            description = "API để staff xem chi tiết một báo cáo sự cố theo ID, bao gồm cả ghi chú xử lý từ Admin."
    )
    @GetMapping("/{id}")
    public ResponseEntity<IncidentReportDto> getIncidentById(
            @Parameter(description = "ID của báo cáo sự cố cần xem") @PathVariable Long id) {
        IncidentReportDto incidentReport = incidentReportStaffService.getIncidentById(id);
        return ResponseEntity.ok(incidentReport);
    }

    @Operation(
            summary = "Staff cập nhật một báo cáo sự cố",
            description = "API để staff cập nhật trạng thái và ghi chú xử lý cho một báo cáo sự cố."
    )
    @PutMapping("/{id}")
    public ResponseEntity<IncidentReportDto> updateIncidentReport(
            @Parameter(description = "ID của báo cáo sự cố cần cập nhật") @PathVariable Long id,
            @RequestBody IncidentReportUpdateRequest request) {
        IncidentReportDto updatedIncident = incidentReportStaffService.updateIncidentReport(id, request);
        return ResponseEntity.ok(updatedIncident);
    }
}
