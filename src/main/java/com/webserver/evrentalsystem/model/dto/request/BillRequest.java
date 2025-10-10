package com.webserver.evrentalsystem.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillRequest {
    @NotNull
    @Schema(description = "Thời gian trả xe", example = "2025-09-27T15:30:00")
    private LocalDateTime returnTime;
}
