package com.webserver.evrentalsystem.controller.admin;

import com.webserver.evrentalsystem.model.dto.entitydto.StaffStationDto;
import com.webserver.evrentalsystem.model.dto.request.AssignStaffRequest;
import com.webserver.evrentalsystem.service.admin.StaffStationAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/staff-stations")
@Tag(name = "4.4. Admin Staff Station", description = "API quản lý nhân viên làm việc tại trạm")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class StaffStationAdminController {

    @Autowired
    private StaffStationAdminService staffStationAdminService;

    @Operation(
            summary = "Gán nhân viên vào trạm",
            description = "Admin gán 1 nhân viên (staff) vào 1 trạm",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Gán thành công",
                            content = @Content(schema = @Schema(implementation = StaffStationDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
                    @ApiResponse(responseCode = "404", description = "User hoặc Station không tồn tại")
            }
    )
    @PostMapping
    public ResponseEntity<StaffStationDto> assignStaff(@Valid @RequestBody AssignStaffRequest request) {
        StaffStationDto dto = staffStationAdminService.assignStaffToStation(request);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Kết thúc phân công tại trạm",
            description = "Kết thúc (deactivate) một phân công theo id",
            parameters = {
                    @Parameter(name = "id", description = "ID của phân công", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Kết thúc phân công thành công",
                            content = @Content(schema = @Schema(implementation = StaffStationDto.class))),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy phân công")
            }
    )
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<StaffStationDto> deactivateAssignment(@PathVariable Long id) {
        StaffStationDto dto = staffStationAdminService.deactivateAssignment(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Danh sách phân công nhân viên theo trạm",
            description = "Lấy danh sách phân công; nếu truyền stationId sẽ lọc cho trạm đó",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Danh sách phân công",
                            content = @Content(schema = @Schema(implementation = StaffStationDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<StaffStationDto>> listAssignments(
            @Parameter(description = "Lọc theo station id", example = "5")
            @RequestParam(required = false, name = "station_id") Long stationId
    ) {
        return ResponseEntity.ok(staffStationAdminService.listAssignments(stationId));
    }
}
