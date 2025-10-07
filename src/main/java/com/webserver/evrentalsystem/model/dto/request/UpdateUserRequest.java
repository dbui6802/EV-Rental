package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    @Schema(description = "Họ và tên", example = "Nguyễn Văn A")
    private String fullName;

    @Schema(description = "Email", example = "staff_new@example.com")
    private String email;

    @Schema(description = "Số điện thoại", example = "0912345678")
    private String phone;

    @Schema(description = "Vai trò", allowableValues = {"renter", "staff", "moderator", "admin"}, example = "staff")
    private String role;

    @Schema(description = "Trạng thái", allowableValues = {"active", "inactive", "banned"}, example = "active")
    private String status;
}
