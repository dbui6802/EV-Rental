package com.webserver.evrentalsystem.model.dto.entitydto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Schema(description = "ID user", example = "1")
    private Long id;

    @Schema(description = "Họ và tên", example = "Nguyễn Văn A")
    private String fullName;

    @Schema(description = "Email", example = "staff@example.com")
    private String email;

    @Schema(description = "Số điện thoại", example = "0987654321")
    private String phone;

    @Schema(description = "Vai trò", example = "staff")
    private String role;

    @Schema(description = "Thời gian tạo", example = "2023-10-01T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Thời gian cập nhật gần nhất", example = "2023-10-05T15:30:00")
    private LocalDateTime updatedAt;
}
