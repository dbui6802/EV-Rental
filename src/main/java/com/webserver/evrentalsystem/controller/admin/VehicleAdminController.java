package com.webserver.evrentalsystem.controller.admin;

import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;
import com.webserver.evrentalsystem.model.dto.request.CreateVehicleRequest;
import com.webserver.evrentalsystem.model.dto.request.UpdateVehicleRequest;
import com.webserver.evrentalsystem.service.admin.VehicleAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/vehicles")
@Tag(name = "4.1. Admin Vehicle", description = "API quản lý phương tiện của Admin")
@SecurityRequirement(name = "bearerAuth")
public class VehicleAdminController {

    @Autowired
    private VehicleAdminService vehicleAdminService;

    @Operation(
            summary = "Thêm xe mới (có ảnh)",
            description = "Admin thêm xe mới vào hệ thống, có thể upload ảnh kèm theo",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = CreateVehicleRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thêm thành công",
                            content = @Content(schema = @Schema(implementation = VehicleDto.class))),
                    @ApiResponse(responseCode = "400", description = "Tham số không hợp lệ")
            }
    )
    @PostMapping(consumes = {"multipart/form-data"})
    public VehicleDto createVehicle(@ModelAttribute CreateVehicleRequest request) {
        return vehicleAdminService.createVehicle(request);
    }

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
        return vehicleAdminService.getAllVehicles(status, plateNumber);
    }

    @Operation(
            summary = "Chi tiết xe",
            description = "Xem chi tiết thông tin 1 xe"
    )
    @GetMapping("/{id}")
    public VehicleDto getVehicleById(@PathVariable Long id) {
        return vehicleAdminService.getVehicleById(id);
    }

    @Operation(
            summary = "Cập nhật thông tin xe (có thể đổi ảnh)",
            description = "Admin cập nhật thông tin xe: biển số, loại, tình trạng, ảnh...",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = UpdateVehicleRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy xe")
            }
    )
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public VehicleDto updateVehicle(
            @PathVariable Long id,
            @ModelAttribute UpdateVehicleRequest request
    ) {
        return vehicleAdminService.updateVehicle(id, request);
    }

    @Operation(
            summary = "Xóa xe",
            description = "Admin xóa xe khỏi hệ thống",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Xóa thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy xe")
            }
    )
    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable Long id) {
        vehicleAdminService.deleteVehicle(id);
    }

    @Operation(
            summary = "Thống kê xe theo trạng thái",
            description = "Trả về số lượng xe theo trạng thái: reserved, available, rented, maintenance"
    )
    @GetMapping("/status")
    public Map<String, Long> getVehicleStatusStatistics() {
        return vehicleAdminService.getVehicleStatusStatistics();
    }
}
