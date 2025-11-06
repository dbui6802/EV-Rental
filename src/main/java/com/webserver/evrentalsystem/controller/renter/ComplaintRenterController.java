package com.webserver.evrentalsystem.controller.renter;

import com.webserver.evrentalsystem.model.dto.entitydto.ComplaintDto;
import com.webserver.evrentalsystem.model.dto.request.ComplaintRequest;
import com.webserver.evrentalsystem.service.renter.ComplaintRenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/renter/complaint")
@Tag(name = "2.4. Renter Complaint", description = "API quản lý khiếu nại của renter")
@SecurityRequirement(name = "bearerAuth")
public class ComplaintRenterController {

    @Autowired
    private ComplaintRenterService complaintRenterService;

    @Operation(
            summary = "Gửi khiếu nại mới",
            description = "Renter gửi khiếu nại về rental hoặc nhân viên liên quan để admin xử lý.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tạo khiếu nại thành công",
                            content = @Content(schema = @Schema(implementation = ComplaintDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
                    @ApiResponse(responseCode = "401", description = "Không có quyền truy cập")
            }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ComplaintDto createComplaint(
            @Parameter(
                    description = "Thông tin khiếu nại",
                    required = true,
                    schema = @Schema(implementation = ComplaintRequest.class)
            )
            @Valid @RequestBody ComplaintRequest request
    ) {
        return complaintRenterService.createComplaint(request);
    }

    @Operation(
            summary = "Xem danh sách khiếu nại của renter",
            description = "Lấy tất cả khiếu nại do renter hiện tại gửi.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Danh sách khiếu nại",
                            content = @Content(schema = @Schema(implementation = ComplaintDto.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Không có quyền truy cập")
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ComplaintDto> getComplaintsOfRenter() {
        return complaintRenterService.getComplaintsOfRenter();
    }

    @Operation(
            summary = "Xem chi tiết khiếu nại",
            description = "Renter xem chi tiết khiếu nại của chính mình, bao gồm phản hồi từ admin (nếu có).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Chi tiết khiếu nại",
                            content = @Content(schema = @Schema(implementation = ComplaintDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy khiếu nại"),
                    @ApiResponse(responseCode = "401", description = "Không có quyền truy cập")
            }
    )
    @GetMapping(value = "/{complaintId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ComplaintDto getComplaintDetail(
            @Parameter(description = "ID của khiếu nại cần xem", example = "123", required = true)
            @PathVariable Long complaintId
    ) {
        return complaintRenterService.getComplaintDetail(complaintId);
    }
}
