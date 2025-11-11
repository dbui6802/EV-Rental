package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

    @Schema(description = "Mật khẩu hiện tại", example = "123456", required = true)
    private String currentPassword;

    @Schema(description = "Mật khẩu mới", example = "newPassword@123", required = true)
    private String newPassword;

    @Schema(description = "Xác nhận mật khẩu mới", example = "newPassword@123", required = true)
    private String confirmNewPassword;
}
