package com.webserver.evrentalsystem.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StaffPerformanceResponse {

    @Schema(description = "ID nhân viên", example = "12")
    private Long staffId;

    @Schema(description = "Tên nhân viên", example = "Nguyễn Văn A")
    private String staffName;

    @Schema(description = "Số lượt giao xe", example = "34")
    private Long deliveryCount;

    @Schema(description = "Số lượt nhận xe", example = "28")
    private Long returnCount;

    @Schema(description = "Số lượng khiếu nại", example = "2")
    private Long complaintCount;

    @Schema(description = "Điểm đánh giá trung bình", example = "4.6")
    private Double avgRating;
}
