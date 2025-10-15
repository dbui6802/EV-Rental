package com.webserver.evrentalsystem.controller.staff;

import com.webserver.evrentalsystem.model.dto.entitydto.DocumentDto;
import com.webserver.evrentalsystem.model.dto.entitydto.UserDto;
import com.webserver.evrentalsystem.service.staff.RenterDocumentStaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/staff/renter-document")
@Tag(name = "3.2. Staff Renter Document", description = "API quản lý tài liệu của User bởi Staff")
@SecurityRequirement(name = "bearerAuth")
public class RenterDocumentStaffController {

    @Autowired
    private RenterDocumentStaffService renterDocumentStaffService;

    @Operation(summary = "Danh sách tất cả user")
    @GetMapping
    public List<UserDto> getAllUsers(
            @Parameter(description = "Tìm theo số điện thoại (chuỗi con)")
            @RequestParam(required = false)
            String phone
    ) {
        return renterDocumentStaffService.getAllUsers(phone);
    }

    @Operation(
            summary = "Lấy danh sách tài liệu của Renter theo ID",
            description = "API cho phép staff lấy toàn bộ danh sách tài liệu (Document) của một renter theo ID"
    )
    @GetMapping("/{renterId}")
    public List<DocumentDto> getDocumentsByRenterId(
            @Parameter(description = "ID của renter", required = true, example = "1")
            @PathVariable Long renterId
    ) {
        return renterDocumentStaffService.getDocumentsByRenterId(renterId);
    }

    @Operation(
            summary = "Xác thực tài liệu của Renter",
            description = "API cho phép staff xác thực một tài liệu (Document) của một renter"
    )
    @PutMapping("/verify/{documentId}")
    public DocumentDto verifyDocument(
            @Parameter(description = "ID của tài liệu cần xác thực", required = true, example = "1")
            @PathVariable Long documentId
    ) {
        return renterDocumentStaffService.verifyDocument(documentId);
    }
}