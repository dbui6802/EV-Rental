package com.webserver.evrentalsystem.controller.staff;

import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;
import com.webserver.evrentalsystem.service.staff.VehicleStaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/staff/vehicle")
@Tag(name = "3.1. Staff Vehicle", description = "API quản lý xe của nhân viên (Staff)")
public class VehicleStaffController {

    @Autowired
    private VehicleStaffService vehicleStaffService;

    @Operation(
            summary = "Danh sách tất cả xe",
            description = "Admin lấy danh sách tất cả xe, có thể filter theo trạng thái hoặc biển số",
            responses = @ApiResponse(responseCode = "200", description = "Danh sách xe")
    )
    @GetMapping
    public List<VehicleDto> getAllVehicles(
            @Parameter(description = "Trạng thái xe (reserved, available, rented, maintenance)") @RequestParam(required = false) String status,
            @Parameter(description = "Biển số xe") @RequestParam(required = false, name = "plate_number") String plateNumber
    ) {
        return vehicleStaffService.getAllVehicles(status, plateNumber);
    }
}
