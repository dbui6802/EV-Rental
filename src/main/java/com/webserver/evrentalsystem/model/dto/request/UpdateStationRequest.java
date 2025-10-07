package com.webserver.evrentalsystem.model.dto.request;

import com.webserver.evrentalsystem.entity.StationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStationRequest {
    @Schema(description = "Tên trạm (có thể null nếu không cập nhật)", example = "Trạm Nguyễn Văn Cừ")
    private String name;

    @Schema(description = "Địa chỉ trạm (có thể null nếu không cập nhật)", example = "123 Nguyễn Văn Cừ, Quận 5, TP.HCM")
    private String address;

    @Schema(description = "Trạng thái trạm (active | inactive | maintenance)  (có thể null nếu không cập nhật)", example = "active")
    private String status;

    @Schema(description = "Vĩ độ (có thể null nếu không cập nhật)", example = "10.762622")
    private Double latitude;

    @Schema(description = "Kinh độ (có thể null nếu không cập nhật)", example = "106.660172")
    private Double longitude;

    @Schema(hidden = true)
    public boolean isValid() {
        try {
            if (status != null) {
                StationStatus.fromValue(status);
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
        return (name == null || !name.isEmpty())
                && (address == null || !address.isEmpty())
                && (latitude == null || (latitude >= -90 && latitude <= 90))
                && (longitude == null || (longitude >= -180 && longitude <= 180));
    }
}
