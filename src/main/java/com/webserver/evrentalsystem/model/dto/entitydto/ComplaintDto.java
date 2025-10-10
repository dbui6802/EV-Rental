package com.webserver.evrentalsystem.model.dto.entitydto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ComplaintDto {

    @Schema(description = "ID khiếu nại", example = "1001")
    private Long id;

    @Schema(description = "Rental liên quan")
    private RentalDto rental;

    @Schema(description = "Renter gửi khiếu nại")
    private UserDto renter;

    @Schema(description = "Nhân viên bị khiếu nại")
    private UserDto staff;

    @Schema(description = "Admin xử lý khiếu nại (nếu có)")
    private UserDto admin;

    @Schema(description = "Mô tả khiếu nại", example = "Xe giao trễ 2 tiếng, không đúng hợp đồng")
    private String description;

    @Schema(description = "Trạng thái khiếu nại", example = "PENDING")
    private String status;

    @Schema(description = "Phản hồi từ admin (nếu có)", example = "Đã xử lý hoàn tiền 20% cho khách")
    private String resolution;

    @Schema(description = "Thời gian tạo khiếu nại", example = "2025-10-04T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Thời gian khiếu nại được giải quyết (nếu có)", example = "2025-10-05T14:20:00")
    private LocalDateTime resolvedAt;
}
