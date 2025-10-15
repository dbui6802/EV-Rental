package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateVehicleRequest {

    @Schema(description = "Biển số xe", example = "30A-12345")
    private String licensePlate;

    @Schema(description = "Loại xe", example = "motorbike", allowableValues = {"car", "motorbike"})
    private String type;

    @Schema(description = "Hãng xe", example = "Honda")
    private String brand;

    @Schema(description = "Mẫu xe", example = "Air Blade")
    private String model;

    @Schema(description = "Dung lượng pin (kWh)", example = "20")
    private Integer capacity;

    @Schema(description = "Quãng đường di chuyển trên mỗi lần sạc đầy (km)", example = "250")
    private Integer rangePerFullCharge;

    @Schema(description = "Trạng thái xe", example = "available", allowableValues = {"reserved", "available", "reserved", "maintenance", "rented"})
    private String status;

    @Schema(description = "Giá thuê mỗi giờ", example = "50000")
    private BigDecimal pricePerHour;

    @Schema(description = "ID trạm", example = "2")
    private Long stationId;

    @Schema(description = "Ảnh xe")
    private MultipartFile image;
}
