package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Yêu cầu hủy đặt chỗ")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CancelReservationRequest {
    @Schema(
            description = "Lý do hủy đặt chỗ",
            example = "Hủy do thay đổi kế hoạch",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String cancelReason;
}
