package com.webserver.evrentalsystem.controller.renter;

import com.webserver.evrentalsystem.entity.ReservationStatus;
import com.webserver.evrentalsystem.entity.VehicleType;
import com.webserver.evrentalsystem.model.dto.entitydto.ReservationDto;
import com.webserver.evrentalsystem.model.dto.entitydto.StationDto;
import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;
import com.webserver.evrentalsystem.model.dto.request.CancelReservationRequest;
import com.webserver.evrentalsystem.model.dto.request.CreateReservationRequest;
import com.webserver.evrentalsystem.service.renter.BookingRenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/renter/booking")
@Tag(name = "2.2. Renter Booking", description = "API quản lý đặt xe của Renter")
@SecurityRequirement(name = "bearerAuth")
public class BookingRenterController {

    @Autowired
    private BookingRenterService bookingRenterService;

    // ================= Stations =================
    @Operation(
            summary = "Xem danh sách trạm",
            description = "Trả về danh sách các trạm cho thuê xe cùng vị trí (latitude, longitude)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Danh sách trạm",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StationDto.class)))
            }
    )
    @GetMapping("/stations")
    public ResponseEntity<List<StationDto>> getStations() {
        List<StationDto> stations = bookingRenterService.getStations();
        return ResponseEntity.ok(stations);
    }

    // ================= Vehicles =================
    @Operation(
            summary = "Xem danh sách xe có sẵn",
            description = "Trả về danh sách xe có sẵn, hỗ trợ lọc theo loại, trạm, giá",
            parameters = {
                    @Parameter(name = "type", description = "Loại xe (motorbike | car)"),
                    @Parameter(name = "station_id", description = "ID trạm"),
                    @Parameter(name = "price_min", description = "Giá thấp nhất"),
                    @Parameter(name = "price_max", description = "Giá cao nhất"),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Danh sách xe",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = VehicleDto.class)))
            }
    )
    @GetMapping("/vehicles")
    public ResponseEntity<List<VehicleDto>> getVehicles(
            @RequestParam(required = false) String type,
            @RequestParam(required = false, name = "station_id") Long stationId,
            @RequestParam(required = false, name = "price_min") Double priceMin,
            @RequestParam(required = false, name = "price_max") Double priceMax
    ) {
        List<VehicleDto> vehicles = bookingRenterService.getVehicles(type, stationId, priceMin, priceMax);
        return ResponseEntity.ok(vehicles);
    }

    // ================= Reservations =================
    @Operation(
            summary = "Tạo đặt xe trước (booking)",
            description = "Người thuê đặt trước xe theo loại hoặc chọn xe cụ thể",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateReservationRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Đặt xe thành công",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationDto.class)))
            }
    )
    @PostMapping("/reservations")
    public ResponseEntity<ReservationDto> createReservation(
            @RequestBody CreateReservationRequest request
    ) {
        ReservationDto reservation = bookingRenterService.createReservation(request);
        return ResponseEntity.ok(reservation);
    }

    @Operation(
            summary = "Xem danh sách booking của tôi",
            parameters = {
                    @Parameter(name = "status", description = "Trạng thái (PENDING, CONFIRMED, CANCELLED, EXPIRED)"),
                    @Parameter(name = "vehicleId", description = "ID xe"),
                    @Parameter(name = "startFrom", description = "Thời gian bắt đầu (>=)", example = "2025-09-23T10:00:00"),
                    @Parameter(name = "startTo", description = "Thời gian bắt đầu (<=)", example = "2025-09-30T23:59:59")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Danh sách booking",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationDto.class)))
            }
    )
    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationDto>> getMyReservations(
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTo
    ) {
        List<ReservationDto> reservations = bookingRenterService.getMyReservations(status, vehicleId, startFrom, startTo);
        return ResponseEntity.ok(reservations);
    }

    @Operation(
            summary = "Chi tiết booking",
            description = "Lấy chi tiết thông tin một booking theo ID",
            parameters = {
                    @Parameter(name = "id", description = "ID của booking", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Chi tiết booking",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationDto.class))),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy booking")
            }
    )
    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long id) {
        ReservationDto reservation = bookingRenterService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @Operation(
            summary = "Hủy booking",
            description = "Người thuê hủy một booking theo ID",
            parameters = {
                    @Parameter(name = "id", description = "ID của booking", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Thông tin hủy booking (lý do hủy)",
                    required = false
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Hủy thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy booking")
            }
    )
    @PatchMapping("/reservations/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable Long id,
            @RequestBody(required = false) CancelReservationRequest request
    ) {
        String reason = (request != null) ? request.getCancelReason() : null;
        bookingRenterService.cancelReservation(id, reason);
        return ResponseEntity.ok().build();
    }
}
