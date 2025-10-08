package com.webserver.evrentalsystem.controller.staff;

import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;
import com.webserver.evrentalsystem.model.dto.request.StaffUpdateVehicleRequest;
import com.webserver.evrentalsystem.model.dto.request.UpdateVehicleRequest;
import com.webserver.evrentalsystem.service.staff.VehicleStaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Operation(
            summary = "Cập nhật thông tin kỹ thuật xe",
            description = "Staff có thể cập nhật một số thông tin xe: mã, mẫu, dung lượng pin, quãng đường di chuyển trên mỗi lần sạc đầy",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy xe")
            }
    )
    @PutMapping("/{id}")
    public VehicleDto updateVehicle(
            @PathVariable Long id,
            @RequestBody StaffUpdateVehicleRequest request
    ) {
        return vehicleStaffService.updateVehicle(id, request);
    }
}
