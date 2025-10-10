package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViolationRequest {

    @NotNull
    @Schema(description = "ID của rental cần ghi nhận chi phí phát sinh", example = "101")
    private Long rentalId;

    @Schema(description = "Mô tả vi phạm / chi phí phát sinh", example = "Xe bị xước nhẹ")
    private String description;

    @NotNull
    @Positive
    @Schema(description = "Số tiền phạt / chi phí phát sinh", example = "500000")
    private BigDecimal fineAmount;
}
