package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResolveComplaintRequest {

    @Schema(description = "ID của khiếu nại cần xử lý", example = "12", required = true)
    @NotNull
    private Long complaintId;

    @Schema(description = "Trạng thái xử lý (resolved hoặc rejected)", example = "resolved", required = true)
    @NotNull
    private String status;

    @Schema(description = "Nội dung hoặc ghi chú giải quyết", example = "Đã liên hệ khách hàng và hoàn tiền 50%")
    private String resolution;
}
