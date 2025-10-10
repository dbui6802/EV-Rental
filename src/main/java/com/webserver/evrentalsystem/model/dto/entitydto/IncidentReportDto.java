package com.webserver.evrentalsystem.model.dto.entitydto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class IncidentReportDto {
    @Schema(description = "ID báo cáo sự cố", example = "1001")
    private Long id;

    @Schema(description = "Xe")
    private VehicleDto vehicle;

    @Schema(description = "Rental liên quan ( nếu có )")
    private RentalDto rental;

    @Schema(description = "Staff báo cáo", example = "7")
    private UserDto staff;

    @Schema(description = "Mô tả sự cố", example = "Xe bị thủng lốp khi đang di chuyển")
    private String description;

    @Schema(description = "Mức độ nghiêm trọng", example = "HIGH")
    private String severity;

    @Schema(description = "Trạng thái báo cáo", example = "PENDING")
    private String status;

    @Schema(description = "Thời gian tạo báo cáo", example = "2025-09-27T10:15:30")
    private LocalDateTime createdAt;
}
