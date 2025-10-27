package com.webserver.evrentalsystem.controller.renter;

import com.webserver.evrentalsystem.model.dto.entitydto.RentalCheckDto;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;
import com.webserver.evrentalsystem.service.renter.RentalRenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/renter/rentals")
@Tag(name = "2.3. Renter Rental", description = "API quản lý thuê xe của người thuê (Renter)")
@SecurityRequirement(name = "bearerAuth")
public class RentalRenterController {

    @Autowired
    private RentalRenterService rentalRenterService;

    @Operation(summary = "Lấy tất cả rental của một renter, có hỗ trợ lọc")
    @GetMapping("/all")
    public List<RentalDto> getAllRentals(
            @Parameter(
                    description = "Trạng thái rental",
                    example = "booked",
                    allowEmptyValue = true,
                    schema = @Schema(
                            type = "string",
                            allowableValues = {
                                    "booked",
                                    "in_use",
                                    "waiting_for_payment",
                                    "returned",
                                    "cancelled"
                            }
                    )
            )
            @RequestParam(required = false) String status,

            @Parameter(description = "Ngày bắt đầu (yyyy-MM-dd)", example = "2025-09-01")
            @RequestParam(required = false) String fromDate,

            @Parameter(description = "Ngày kết thúc (yyyy-MM-dd)", example = "2025-09-30")
            @RequestParam(required = false) String toDate
    ) {
        return rentalRenterService.getAllRentalsOfRenter(status, fromDate, toDate);
    }

    @Operation(summary = "Xem biên bản giao/nhận xe")
    @GetMapping("/{id}/checks")
    public List<RentalCheckDto> getRentalChecks(
            @Parameter(description = "ID lượt thuê", example = "101")
            @PathVariable Long id) {
        return rentalRenterService.getRentalChecks(id);
    }

    @Operation(
            summary = "Xác nhận đã nhận xe",
            description = "Người thuê xác nhận đã nhận xe sau khi nhân viên xác nhận đã giao xe",
            parameters = {
                    @Parameter(name = "id", description = "ID lượt thuê", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Hủy thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy lượt thuê")
            }
    )
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable Long id
    ) {
        rentalRenterService.confirmInUse(id);
        return ResponseEntity.ok().build();
    }
}
