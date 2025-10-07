package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalReturnRequest {

    @NotNull(message = "station_return_id không được null")
    @Schema(description = "ID trạm trả xe", example = "7", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long stationReturnId;
}
