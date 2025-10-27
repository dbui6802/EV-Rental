package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

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

    @NotNull(message = "Phần trăm pin không được để trống")
    @Range(min = 0, max = 100, message = "Phần trăm pin không hợp lệ. Vui lòng nhập giá trị từ 0 đến 100")
    @Schema(description = "Phần trăm pin xe. Nhập từ 0 đến 100", example = "85")
    Integer batteryLevel; // 0 to 100

    @NotNull(message = "Odo không được để trống")
    @Positive(message = "Odometer vui lòng nhập giá trị >= 0")
    @Schema(description = "Odometer (km)", example = "100000")
    Integer odo; // 0 to 100

}
