package com.webserver.evrentalsystem.model.dto.request;

import com.webserver.evrentalsystem.entity.StationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStationRequest {
    @Schema(description = "Tên trạm", example = "Trạm Nguyễn Văn Cừ", required = true)
    private String name;

    @Schema(description = "Địa chỉ trạm", example = "123 Nguyễn Văn Cừ, Quận 5, TP.HCM", required = true)
    private String address;

    @Schema(description = "Trạng thái trạm (active | inactive | maintenance)", example = "active", required = true)
    private String status;

    @Schema(description = "Vĩ độ", example = "10.762622", required = true)
    private Double latitude;

    @Schema(description = "Kinh độ", example = "106.660172", required = true)
    private Double longitude;

    @Schema(hidden = true)
    public boolean isValid() {
        try {
            StationStatus.fromValue(status);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return name != null && !name.isEmpty()
                && address != null && !address.isEmpty()
                && latitude != null
                && longitude != null;
    }
}
