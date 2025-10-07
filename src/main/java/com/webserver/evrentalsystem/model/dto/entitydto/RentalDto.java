package com.webserver.evrentalsystem.model.dto.entitydto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalDto {

    @Schema(description = "ID lượt thuê", example = "101")
    private Long id;

    @Schema(description = "Thông tin người thuê")
    private UserDto renter;

    @Schema(description = "Thông tin xe")
    private VehicleDto vehicle;

    @Schema(description = "Trạm giao xe")
    private StationDto stationPickup;

    @Schema(description = "Trạm nhận xe")
    private StationDto stationReturn;

    @Schema(description = "Nhân viên giao xe")
    private UserDto staffPickup;

    @Schema(description = "Nhân viên nhận xe")
    private UserDto staffReturn;

    @Schema(description = "Thời gian bắt đầu thuê")
    private LocalDateTime startTime;

    @Schema(description = "Thời gian kết thúc thuê")
    private LocalDateTime endTime;

    @Schema(description = "Quãng đường di chuyển (km)", example = "25.7")
    private Double totalDistance;

    @Schema(description = "Tổng chi phí", example = "350000")
    private BigDecimal totalCost;

    @Schema(description = "Loại thuê (booking/walk-in)", example = "booking")
    private String rentalType;

    @Schema(description = "Tiền đặt cọc", example = "500000")
    private BigDecimal depositAmount;

    @Schema(description = "Trạng thái đặt cọc", example = "held")
    private String depositStatus;

    @Schema(description = "Trạng thái lượt thuê", example = "in_use")
    private String status;

    @Schema(description = "Ngày tạo record")
    private LocalDateTime createdAt;
}

