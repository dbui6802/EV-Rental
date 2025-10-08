package com.webserver.evrentalsystem.controller.admin;

import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;
import com.webserver.evrentalsystem.service.admin.RentalAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/admin/rental")
@Tag(name = "4.5. Admin Rental", description = "API quản lý đơn thuê xe")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class RentalAdminController {

    @Autowired
    private RentalAdminService rentalAdminService;

    @Operation(
            summary = "Lấy danh sách Rental theo bộ lọc",
            description = "API cho phép staff lấy danh sách rental với các điều kiện lọc theo trạng thái, renter, vehicle, station pickup/return, khoảng thời gian."
    )
    @GetMapping
    public List<RentalDto> getRentals(
            @Parameter(description = "ID của renter (người thuê)")
            @RequestParam(required = false) Long renterId,

            @Parameter(description = "ID của vehicle (xe)")
            @RequestParam(required = false) Long vehicleId,

            @Parameter(description = "ID trạm pickup")
            @RequestParam(required = false) Long stationPickupId,

            @Parameter(description = "ID trạm return")
            @RequestParam(required = false) Long stationReturnId,

            @Parameter(
                    description = "Trạng thái của Rental",
                    example = "in_use",
                    schema = @Schema(allowableValues = {"booked", "in_use", "returned", "waiting_for_payment", "cancelled"})
            )
            @RequestParam(required = false) String status,

            @Parameter(description = "Thời gian bắt đầu (lọc từ)", example = "2025-09-23T08:00:00")
            @RequestParam(required = false) LocalDateTime startFrom,

            @Parameter(description = "Thời gian bắt đầu (lọc đến)", example = "2025-09-23T18:00:00")
            @RequestParam(required = false) LocalDateTime startTo
    ) {
        return rentalAdminService.getRentals(renterId, vehicleId, stationPickupId, stationReturnId, status, startFrom, startTo);
    }
}
