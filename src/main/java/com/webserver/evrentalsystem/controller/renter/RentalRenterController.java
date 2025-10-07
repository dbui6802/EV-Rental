package com.webserver.evrentalsystem.controller.renter;

import com.webserver.evrentalsystem.model.dto.entitydto.RentalCheckDto;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;
import com.webserver.evrentalsystem.model.dto.request.*;
import com.webserver.evrentalsystem.service.renter.RentalRenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/renter/rentals")
@Tag(name = "2.3. Renter Rental", description = "API quản lý thuê xe của người thuê (Renter)")
@SecurityRequirement(name = "bearerAuth")
public class RentalRenterController {

    @Autowired
    private RentalRenterService rentalRenterService;

    @Operation(summary = "Xem lượt thuê đang hoạt động")
    @GetMapping("/current")
    public List<RentalDto> getCurrentRentals() {
        return rentalRenterService.getCurrentRentals();
    }

    @Operation(summary = "Xem biên bản giao/nhận xe")
    @GetMapping("/{id}/checks")
    public List<RentalCheckDto> getRentalChecks(
            @Parameter(description = "ID lượt thuê", example = "101")
            @PathVariable Long id) {
        return rentalRenterService.getRentalChecks(id);
    }

    @Operation(summary = "Trả xe tại trạm")
    @PostMapping("/{id}/return")
    public RentalDto returnVehicle(
            @Parameter(description = "ID lượt thuê", example = "101")
            @PathVariable Long id,
            @Valid @RequestBody RentalReturnRequest request) {
        return rentalRenterService.returnVehicle(id, request);
    }
}
