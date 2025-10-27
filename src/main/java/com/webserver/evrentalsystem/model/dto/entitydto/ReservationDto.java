package com.webserver.evrentalsystem.model.dto.entitydto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    @Schema(
            description = "ID đặt chỗ",
            example = "1001"
    )
    private Long id;

    @Schema(
            description = "Người thuê thực hiện đặt chỗ",
            implementation = UserDto.class
    )
    private UserDto renter;

    @Schema(
            description = "Xe cụ thể được đặt (nếu có). Có thể null nếu đặt theo loại xe",
            implementation = VehicleDto.class
    )
    private VehicleDto vehicle;

    @Schema(
            description = "Thời gian bắt đầu đặt xe (ISO 8601 timestamp)",
            example = "2025-09-22T14:00:00Z"
    )
    private LocalDateTime reservedStartTime;

    @Schema(
            description = "Thời gian kết thúc đặt xe (ISO 8601 timestamp)",
            example = "2025-09-22T18:00:00Z"
    )
    private LocalDateTime reservedEndTime;

    @Schema(
            description = "Người hủy đặt chỗ (nếu có)",
            implementation = UserDto.class
    )
    private UserDto cancelledBy;

    @Schema(
            description = "Lý do hủy đặt chỗ (nếu có)",
            example = "Renter did not show up"
    )
    private String cancelledReason;

    @Schema(
            description = "Trạng thái đặt chỗ. Giá trị hợp lệ: " +
                    "`pending` (chờ xác nhận), " +
                    "`confirmed` (đã xác nhận), " +
                    "`cancelled` (đã hủy), " +
                    "`expired` (hết hạn)",
            example = "pending"
    )
    private String status;

    @Schema(
            description = "Thời điểm tạo đặt chỗ (ISO 8601 timestamp)",
            example = "2025-09-21T09:30:00Z"
    )
    private LocalDateTime createdAt;

    @Schema(description = "Số tiền bảo hiểm nếu có", example = "200")
    private BigDecimal insurance;

}
