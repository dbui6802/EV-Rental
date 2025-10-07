package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "Thông tin tài liệu upload")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentUploadRequest {
    @Schema(description = "Loại giấy tờ (CCCD | CMND | GPLX)", example = "CCCD", required = true)
    private String type;

    @Schema(description = "Số giấy tờ", example = "123456789", required = true)
    private String documentNumber;
}
