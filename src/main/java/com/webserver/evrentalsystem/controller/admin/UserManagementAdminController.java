package com.webserver.evrentalsystem.controller.admin;

import com.webserver.evrentalsystem.model.dto.entitydto.DocumentDto;
import com.webserver.evrentalsystem.model.dto.entitydto.UserDto;
import com.webserver.evrentalsystem.model.dto.request.CreateUserRequest;
import com.webserver.evrentalsystem.model.dto.request.UpdateUserRequest;
import com.webserver.evrentalsystem.service.admin.UserManagementAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/users")
@Tag(name = "4.3. Admin User Management", description = "API quản lý nhân viên và admin")
@SecurityRequirement(name = "bearerAuth")
public class UserManagementAdminController {

    @Autowired
    private UserManagementAdminService userManagementAdminService;

    @Operation(summary = "Tạo tài khoản mới (staff hoặc admin)")
    @ApiResponse(responseCode = "200", description = "Tạo user thành công",
            content = @Content(schema = @Schema(implementation = UserDto.class)))
    @PostMapping
    public UserDto createUser(@Valid @RequestBody CreateUserRequest request) {
        return userManagementAdminService.createUser(request);
    }

    @Operation(summary = "Danh sách tất cả user")
    @GetMapping
    public List<UserDto> getAllUsers(
            @Parameter(description = "Lọc theo role: renter, staff, admin")
            @RequestParam(required = false)
            @Pattern(regexp = "^(renter|staff|admin)$", message = "Role phải là 'renter', 'staff' hoặc 'admin'")
            String role,

            @Parameter(description = "Tìm theo số điện thoại (chuỗi con)")
            @RequestParam(required = false)
            String phone
    ) {
        return userManagementAdminService.getAllUsers(role, phone);
    }

    @Operation(summary = "Thông tin chi tiết user")
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userManagementAdminService.getUserById(id);
    }

    @Operation(summary = "Cập nhật thông tin user")
    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        return userManagementAdminService.updateUser(id, request);
    }

    @Operation(summary = "Xóa user")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userManagementAdminService.deleteUser(id);
    }

    @Operation(summary = "Lấy thông tin hồ sơ của khách hàng")
    @GetMapping("/profile/{renterId}")
    public List<DocumentDto> getRenterDocument(@PathVariable Long renterId) {
        return userManagementAdminService.getRenterDocument(renterId);
    }
}
