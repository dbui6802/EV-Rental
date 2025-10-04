package com.webserver.evrentalsystem.model.dto.entitydto;

import com.webserver.evrentalsystem.entity.DocumentType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {
    private Long id;
    private UserDto user;
    private DocumentType type;
    private String documentNumber;
    private String documentUrl;
    private Boolean verified;
    private UserDto verifiedBy;
    private LocalDateTime createdAt;
}