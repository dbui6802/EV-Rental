package com.webserver.evrentalsystem.model.dto.entitydto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalCheckDto {

    @Schema(description = "ID biên bản giao/nhận xe", example = "501")
    private Long id;

    @Schema(description = "Thông tin lượt thuê")
    private RentalDto rental;

    @Schema(description = "Nhân viên thực hiện check")
    private UserDto staff;

    @Schema(description = "Loại check (pickup/return)", example = "pickup")
    private String checkType;

    @Schema(description = "Báo cáo tình trạng xe", example = "Xe có trầy xước nhẹ bên phải")
    private String conditionReport;

    @Schema(description = "URL ảnh tình trạng xe", example = "https://cdn.example.com/photos/rental_501.jpg")
    private String photoUrl;

    @Schema(description = "Chữ ký khách hàng", example = "https://cdn.example.com/signatures/customer_501.png")
    private String customerSignatureUrl;

    @Schema(description = "Chữ ký nhân viên", example = "https://cdn.example.com/signatures/staff_501.png")
    private String staffSignatureUrl;

    @Schema(description = "Thời điểm lập biên bản")
    private LocalDateTime createdAt;
}

