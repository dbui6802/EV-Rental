package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalCheckInRequest {

    @Schema(description = "ID người thuê xe", example = "789")
    @NotNull(message = "renter_id không được null")
    private Long renterId;

    @Schema(description = "ID đặt chỗ (nếu check-in từ reservation)", example = "123")
    private Long reservationId;

    @Schema(description = "ID xe (nếu walk-in)", example = "45")
    private Long vehicleId;

    @NotNull(message = "station_id không được null")
    @Schema(description = "ID trạm nhận xe", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long stationId;

    @Schema(description = "Thời gian bắt đầu thuê", example = "2024-08-01T10:00:00")
    private LocalDateTime startTime;

    @Schema(description = "Thời gian kết thúc thuê dự kiến", example = "2024-08-01T12:00:00")
    private LocalDateTime endTime;

    @Schema(description = "Số tiền đặt cọc (nếu có)", example = "150")
    private BigDecimal depositAmount;
}
