package com.webserver.evrentalsystem.controller.admin;

import com.webserver.evrentalsystem.model.dto.entitydto.StationDto;
import com.webserver.evrentalsystem.model.dto.request.CreateStationRequest;
import com.webserver.evrentalsystem.model.dto.request.UpdateStationRequest;
import com.webserver.evrentalsystem.service.admin.StationAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/stations")
@Tag(name = "4.2. Admin Station", description = "API quản lý trạm của Admin")
@SecurityRequirement(name = "bearerAuth")
public class StationAdminController {

    @Autowired
    private StationAdminService stationAdminService;

    @Operation(
            summary = "Tạo trạm mới",
            description = "Admin tạo một trạm mới",
            requestBody = @RequestBody(
                    description = "Thông tin trạm cần tạo",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateStationRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tạo trạm thành công",
                            content = @Content(schema = @Schema(implementation = StationDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
            }
    )
    @PostMapping
    public ResponseEntity<StationDto> createStation(
            @org.springframework.web.bind.annotation.RequestBody CreateStationRequest request) {
        return ResponseEntity.ok(stationAdminService.createStation(request));
    }

    @Operation(
            summary = "Danh sách tất cả trạm",
            description = "Lấy danh sách tất cả trạm",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Danh sách trạm",
                            content = @Content(schema = @Schema(implementation = StationDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<StationDto>> getAllStations() {
        return ResponseEntity.ok(stationAdminService.getAllStations());
    }

    @Operation(
            summary = "Chi tiết trạm",
            description = "Lấy thông tin chi tiết của một trạm theo ID",
            parameters = {
                    @Parameter(name = "id", description = "ID của trạm", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thông tin chi tiết trạm",
                            content = @Content(schema = @Schema(implementation = StationDto.class))),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy trạm")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<StationDto> getStationById(@PathVariable Long id) {
        return ResponseEntity.ok(stationAdminService.getStationById(id));
    }

    @Operation(
            summary = "Cập nhật thông tin trạm",
            description = "Admin cập nhật tên, địa chỉ, vĩ độ, kinh độ của trạm",
            parameters = {
                    @Parameter(name = "id", description = "ID của trạm", required = true)
            },
            requestBody = @RequestBody(
                    description = "Thông tin cập nhật trạm",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateStationRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cập nhật thành công",
                            content = @Content(schema = @Schema(implementation = StationDto.class))),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy trạm"),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<StationDto> updateStation(
            @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody UpdateStationRequest request) {
        return ResponseEntity.ok(stationAdminService.updateStation(id, request));
    }

    @Operation(
            summary = "Xóa trạm",
            description = "Admin xóa một trạm theo ID",
            parameters = {
                    @Parameter(name = "id", description = "ID của trạm", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Xóa thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy trạm")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationAdminService.deleteStation(id);
        return ResponseEntity.ok().build();
    }
}
