package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Yêu cầu xác nhận lượt thuê")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmRentalRequest {
    @Schema(description = "ID lượt thuê cần xác nhận", example = "101")
    @NotNull(message = "rentalId cannot be null")
    @Positive(message = "rentalId must be positive")
    Long rentalId;

    @Schema(description = "Loại xác nhận: 'pickup' để nhận xe, 'return' để trả xe", example = "pickup")
    @NotNull
    String checkType; // "pickup" or "return"

    @Schema(description = "Báo cáo tình trạng xe (nếu có)", example = "Xe có trầy xước nhẹ bên phải")
    String conditionReport; // Optional report on vehicle condition
}
