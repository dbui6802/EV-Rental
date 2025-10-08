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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/staff/incident-report")
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
}
