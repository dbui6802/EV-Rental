package com.webserver.evrentalsystem.controller.admin;

import com.webserver.evrentalsystem.model.dto.entitydto.ComplaintDto;
import com.webserver.evrentalsystem.model.dto.request.ResolveComplaintRequest;
import com.webserver.evrentalsystem.service.admin.ComplaintAdminService;
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

@RestController
@RequestMapping("api/admin/complaint")
@Tag(name = "4.6. Admin Complaint", description = "API quản lý khiếu nại của admin")
@SecurityRequirement(name = "bearerAuth")
public class ComplaintAdminController {

    @Autowired
    private ComplaintAdminService complaintAdminService;

    @Operation(
            summary = "Lấy danh sách khiếu nại",
            description = "API cho phép admin xem tất cả khiếu nại, có thể lọc theo trạng thái (pending, resolved, rejected).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công",
                            content = @Content(schema = @Schema(implementation = ComplaintDto.class))),
                    @ApiResponse(responseCode = "401", description = "Không có quyền truy cập", content = @Content)
            }
    )
    @GetMapping
    public List<ComplaintDto> getAllComplaints(
            @Parameter(
                    description = "Trạng thái khiếu nại để lọc (optional)",
                    example = "pending",
                    allowEmptyValue = true,
                    schema = @Schema(allowableValues = {"pending", "resolved", "rejected"})
            )
            @RequestParam(required = false) String status) {
        return complaintAdminService.getAllComplaints(status);
    }

    @Operation(
            summary = "Xác nhận xử lý khiếu nại",
            description = "API cho phép admin xác nhận giải quyết hoặc từ chối một khiếu nại, kèm ghi chú nội dung xử lý.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Thông tin xử lý khiếu nại",
                    content = @Content(schema = @Schema(implementation = ResolveComplaintRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cập nhật thành công",
                            content = @Content(schema = @Schema(implementation = ComplaintDto.class))),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy khiếu nại", content = @Content)
            }
    )
    @PostMapping("/resolve")
    public ComplaintDto resolveComplaint(
            @RequestBody ResolveComplaintRequest request
    ) {
        return complaintAdminService.resolveComplaint(request);
    }
}
