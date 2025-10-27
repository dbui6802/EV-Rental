package com.webserver.evrentalsystem.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Thông tin tạo đặt chỗ")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationRequest {

    @Schema(
            description = "ID xe cụ thể nếu chọn một xe nhất định, có thể bỏ trống nếu chỉ chọn loại xe",
            example = "123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long vehicleId;

    @Schema(
            description = "Thời gian bắt đầu đặt xe (ISO 8601 timestamp)",
            example = "2025-09-22T08:00:00"
    )
    private LocalDateTime reservedStartTime;

    @Schema(
            description = "Thời gian kết thúc đặt xe (ISO 8601 timestamp)",
            example = "2025-09-22T18:00:00"
    )
    private LocalDateTime reservedEndTime;

    @Schema(description = "Số tiền bảo hiểm nếu có", example = "200")
    private BigDecimal insurance;

}
