package com.webserver.evrentalsystem.controller.admin;

import com.webserver.evrentalsystem.model.dto.entitydto.ViolationDto;
import com.webserver.evrentalsystem.service.admin.ViolationAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/admin/violation")
@Tag(name = "4.7. Admin Violation", description = "API quản lý vi phạm của admin")
@SecurityRequirement(name = "bearerAuth")
public class ViolationAdminController {

    @Autowired
    private ViolationAdminService violationAdminService;

    @Operation(
            summary = "Lấy danh sách vi phạm của renter",
            description = "API cho phép admin xem danh sách tất cả các vi phạm (violation) được ghi nhận, "
                    + "có thể lọc theo rentalId hoặc renterId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công",
                            content = @Content(schema = @Schema(implementation = ViolationDto.class))),
                    @ApiResponse(responseCode = "401", description = "Không có quyền truy cập", content = @Content)
            }
    )
    @GetMapping
    public List<ViolationDto> getViolations(
            @Parameter(description = "ID của renter để lọc (optional)", example = "15")
            @RequestParam(required = false) Long renterId,

            @Parameter(description = "ID của rental để lọc (optional)", example = "101")
            @RequestParam(required = false) Long rentalId
    ) {
        return violationAdminService.getViolations(renterId, rentalId);
    }
}
