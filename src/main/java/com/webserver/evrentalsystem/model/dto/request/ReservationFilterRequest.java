package com.webserver.evrentalsystem.model.dto.request;

import com.webserver.evrentalsystem.entity.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ReservationFilterRequest", description = "Request lọc danh sách Reservation theo nhiều tiêu chí")
public class ReservationFilterRequest {

    @Schema(description = "ID của renter (người thuê)", example = "101", nullable = true)
    @Positive(message = "renterId phải là số dương")
    private Long renterId;

    @Schema(description = "ID của vehicle (xe)", example = "205", nullable = true)
    @Positive(message = "vehicleId phải là số dương")
    private Long vehicleId;

    @Schema(
            description = "Trạng thái của Reservation",
            example = "pending",
            allowableValues = {"pending", "confirmed", "cancelled", "expired"},
            nullable = true
    )
    private String status;

    @Schema(description = "Thời gian bắt đầu (lọc từ)", example = "2025-09-23T08:00:00", nullable = true)
    @FutureOrPresent(message = "startFrom phải là thời điểm hiện tại hoặc trong tương lai")
    private LocalDateTime startFrom;

    @Schema(description = "Thời gian bắt đầu (lọc đến)", example = "2025-09-30T18:00:00", nullable = true)
    @FutureOrPresent(message = "startTo phải là thời điểm hiện tại hoặc trong tương lai")
    private LocalDateTime startTo;
}

