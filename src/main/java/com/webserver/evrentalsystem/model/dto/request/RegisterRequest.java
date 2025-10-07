package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Schema(description = "Họ và tên đầy đủ của người dùng", example = "Tài khoản Test")
    private String fullName;

    @Schema(description = "Địa chỉ email của người dùng", example = "testemail@gmail.com")
    private String email;

    @Schema(description = "Số điện thoại", example = "0999999999")
    private String phone;

    @Schema(description = "Mật khẩu đăng ký", example = "TestPassword@123")
    private String password;
}
