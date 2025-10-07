package com.webserver.evrentalsystem.model.dto.entitydto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationDto {
    @Schema(description = "ID trạm", example = "1")
    private Long id;

    @Schema(description = "Tên trạm", example = "Trạm Nguyễn Văn Cừ")
    private String name;

    @Schema(description = "Địa chỉ trạm", example = "123 Nguyễn Văn Cừ, Quận 5, TP.HCM")
    private String address;

    @Schema(description = "Trạng thái trạm", example = "active")
    private String status;

    @Schema(description = "Vĩ độ", example = "10.762622")
    private Double latitude;

    @Schema(description = "Kinh độ", example = "106.660172")
    private Double longitude;
}
