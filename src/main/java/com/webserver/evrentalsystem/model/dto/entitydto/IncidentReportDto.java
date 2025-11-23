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

    @Schema(description = "Xe liên quan đến sự cố")
    private VehicleDto vehicle;

    @Schema(description = "Người thuê xe báo cáo")
    private UserDto renter;

    @Schema(description = "Nhân viên chịu trách nhiệm xử lý")
    private UserDto staff;

    @Schema(description = "Mô tả sự cố", example = "Xe bị thủng lốp khi đang di chuyển")
    private String description;

    @Schema(description = "Trạng thái báo cáo", example = "PENDING")
    private String status;

    @Schema(description = "Ghi chú xử lý", example = "Đã thay lốp tại Gara ABC")
    private String resolutionNotes;

    @Schema(description = "Thời gian tạo báo cáo", example = "2025-09-27T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Thời gian xử lý xong báo cáo", example = "2025-09-27T11:00:00")
    private LocalDateTime resolvedAt;

    @Schema(description = "ID của hợp đồng thuê xe liên quan", example = "52")
    private Long rentalId;
}
