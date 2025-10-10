package com.webserver.evrentalsystem.model.dto.request;

import com.webserver.evrentalsystem.entity.IncidentSeverity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidentReportRequest {

    @Schema(description = "ID của xe gặp sự cố", example = "15", required = true)
    @NotNull
    private Long vehicleId;

    @Schema(description = "ID của rental liên quan (nếu có)", example = "101")
    private Long rentalId;

    @Schema(description = "Mô tả chi tiết sự cố", example = "Xe bị thủng lốp khi đang di chuyển", required = true)
    @NotBlank
    private String description;

    @Schema(description = "Mức độ nghiêm trọng của sự cố", example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH"}, required = true)
    @NotNull
    private IncidentSeverity severity;
}

