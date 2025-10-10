package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequest {

    @Schema(description = "ID của chuyến thuê", example = "12", required = true)
    private Long rentalId;

    @Schema(description = "ID của nhân viên (chỉ dùng khi đánh giá nhân viên)", example = "5")
    private Long staffId;

    @Schema(description = "Điểm đánh giá (1 - 5)", example = "4", required = true)
    @Positive(message = "Điểm đánh giá phải là số dương.")
    @Min(value = 1, message = "Điểm đánh giá phải từ 1 đến 5.")
    @Max(value = 5, message = "Điểm đánh giá phải từ 1 đến 5.")
    private Integer rating;

    @Schema(description = "Nhận xét thêm của người thuê", example = "Xe sạch sẽ, nhân viên nhiệt tình.")
    private String comment;
}
