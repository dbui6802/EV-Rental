package com.webserver.evrentalsystem.model.dto.entitydto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffStationDto {

    @Schema(description = "ID bản ghi phân công", example = "1")
    private Long id;

    @Schema(description = "Thông tin nhân viên (user)", implementation = UserDto.class)
    private UserDto staff;

    @Schema(description = "Thông tin trạm", implementation = StationDto.class)
    private StationDto station;

    @Schema(description = "Thời điểm gán nhân viên vào trạm (ISO 8601)", example = "2025-09-23T12:00:00")
    private LocalDateTime assignedAt;

    @Schema(description = "Thời điểm kết thúc phân công (nếu có)", example = "2025-09-30T15:00:00")
    private LocalDateTime deactivatedAt;

    @Schema(description = "Trạng thái phân công: true = đang hoạt động, false = đã kết thúc", example = "true")
    private Boolean isActive;
}
