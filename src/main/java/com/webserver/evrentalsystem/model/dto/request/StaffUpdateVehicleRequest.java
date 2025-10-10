package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffUpdateVehicleRequest {

    @Schema(description = "Hãng xe", example = "Honda")
    private String brand;

    @Schema(description = "Mẫu xe", example = "Air Blade")
    private String model;

    @Schema(description = "Dung lượng pin (kWh)", example = "20")
    private Integer capacity;

    @Schema(description = "Quãng đường di chuyển trên mỗi lần sạc đầy (km)", example = "250")
    private Integer rangePerFullCharge;
}
