package com.webserver.evrentalsystem.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Kết quả thống kê doanh thu tổng và doanh thu theo từng trạm")
public class RevenueResponse {

    @Schema(description = "Tổng doanh thu trong khoảng thời gian", example = "12500000.00")
    private BigDecimal totalRevenue;

    @Schema(description = "Bảng doanh thu theo từng trạm (tên trạm → doanh thu)", example = "{\"Station A\": 5000000.00, \"Station B\": 7500000.00}")
    private Map<String, BigDecimal> revenueByStation;
}
