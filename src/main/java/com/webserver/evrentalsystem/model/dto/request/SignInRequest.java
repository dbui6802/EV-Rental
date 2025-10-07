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
public class SignInRequest {

    @Schema(description = "Số điện thoại của người dùng", example = "0999999999")
    private String phone;

    @Schema(description = "Mật khẩu đăng nhập", example = "TestPassword@123")
    private String password;
}
