package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintRequest {

    @Schema(description = "ID của rental liên quan đến khiếu nại", example = "105", required = true)
    @NotNull
    private Long rentalId;

    @Schema(description = "ID của nhân viên bị khiếu nại (nếu có)", example = "7")
    private Long staffId;

    @Schema(description = "Nội dung chi tiết khiếu nại", example = "Xe giao trễ 2 tiếng, không đúng hợp đồng", required = true)
    @NotBlank
    private String description;
}
