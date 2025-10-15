package com.webserver.evrentalsystem.model.dto.entitydto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDto {

    @Schema(description = "ID xe", example = "1")
    private Long id;

    @Schema(description = "Biển số xe", example = "30A-12345")
    private String licensePlate;

    @Schema(description = "Loại xe", example = "car")
    private String type;

    @Schema(description = "Hãng xe", example = "Toyota")
    private String brand;

    @Schema(description = "Mẫu xe", example = "Vios")
    private String model;

    @Schema(description = "Dung lượng pin (kWh)", example = "40")
    private Integer capacity;

    @Schema(description = "Quãng đường di chuyển trên mỗi lần sạc đầy (km)", example = "250")
    private Integer rangePerFullCharge;

    @Schema(description = "Trạng thái", example = "available")
    private String status;

    @Schema(description = "Giá thuê mỗi giờ", example = "150000")
    private BigDecimal pricePerHour;

    @Schema(description = "Trạm mà xe đang ở")
    private StationDto station;

    @Schema(description = "Đường dẫn ảnh xe")
    private String imageUrl;
}
