package com.webserver.evrentalsystem.controller.staff;

import com.webserver.evrentalsystem.model.dto.entitydto.IncidentReportDto;
import com.webserver.evrentalsystem.model.dto.request.IncidentReportRequest;
import com.webserver.evrentalsystem.service.staff.IncidentReportStaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/staff/incident-reports")
@Tag(name = "3.4. Staff Incident Report", description = "API quản lý báo cáo sự cố bởi Staff")
@SecurityRequirement(name = "bearerAuth")
public class IncidentReportStaffController {

    @Autowired
    private IncidentReportStaffService incidentReportStaffService;

    @Operation(
            summary = "Tạo báo cáo sự cố mới",
            description = "Staff gửi báo cáo sự cố hỏng hóc của xe cho admin xử lý.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tạo báo cáo thành công",
                            content = @Content(schema = @Schema(implementation = IncidentReportDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dữ liệu không hợp lệ"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Không có quyền truy cập"
                    )
            }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public IncidentReportDto createIncidentReport(
            @Parameter(
                    description = "Thông tin báo cáo sự cố",
                    required = true,
                    schema = @Schema(implementation = IncidentReportRequest.class)
            )
            @Valid @RequestBody IncidentReportRequest request
    ) {
        return incidentReportStaffService.createIncidentReport(request);
    }

    @Operation(
            summary = "Staff xem danh sách báo cáo sự cố",
            description = "API để staff xem danh sách tất cả báo cáo sự cố, có thể lọc theo trạng thái."
    )
    @GetMapping
    public ResponseEntity<List<IncidentReportDto>> getAllIncidents(
            @Parameter(description = "Lọc theo trạng thái sự cố (ví dụ: PENDING, IN_REVIEW, RESOLVED)")
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(incidentReportStaffService.getAllIncidents(status));
    }

    @Operation(
            summary = "Staff xem chi tiết một báo cáo sự cố",
            description = "API để staff xem chi tiết một báo cáo sự cố theo ID, bao gồm cả ghi chú xử lý từ Admin."
    )
    @GetMapping("/{id}")
    public ResponseEntity<IncidentReportDto> getIncidentById(
            @Parameter(description = "ID của báo cáo sự cố cần xem")
            @PathVariable Long id) {
        return ResponseEntity.ok(incidentReportStaffService.getIncidentById(id));
    }
}
