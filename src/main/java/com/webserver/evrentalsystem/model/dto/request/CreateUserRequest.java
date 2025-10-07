package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    @Schema(description = "Họ và tên", example = "Nguyễn Văn A", required = true)
    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    @Schema(description = "Email (unique)", example = "staff@example.com", required = true)
    @Email(message = "Email không hợp lệ")
    private String email;

    @Schema(description = "Số điện thoại (unique)", example = "0987654321", required = true)
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(\\+84|0)\\d{9}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @Schema(description = "Mật khẩu", example = "123456", required = true)
    @NotBlank(message = "Mật khẩu không được để trống")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
            message = "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ cái, số và có thể chứa ký tự đặc biệt"
    )
    private String password;

    @Schema(description = "Vai trò", allowableValues = {"staff", "admin"}, example = "staff", required = true)
    @NotBlank(message = "Vai trò không được để trống")
    @Pattern(regexp = "^(staff|admin)$", message = "Vai trò phải là 'staff' hoặc 'admin'")
    private String role;
}
