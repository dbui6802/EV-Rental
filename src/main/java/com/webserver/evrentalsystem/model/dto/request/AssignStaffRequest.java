package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignStaffRequest {

    @Schema(description = "ID của nhân viên (user) cần gán", example = "10", required = true)
    @NotNull(message = "staffId không được để trống")
    private Long staffId;

    @Schema(description = "ID của trạm", example = "5", required = true)
    @NotNull(message = "stationId không được để trống")
    private Long stationId;
}
