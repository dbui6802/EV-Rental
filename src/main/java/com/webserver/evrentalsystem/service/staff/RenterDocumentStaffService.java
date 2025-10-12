package com.webserver.evrentalsystem.service.staff;

import com.webserver.evrentalsystem.model.dto.entitydto.DocumentDto;

import java.util.List;

public interface RenterDocumentStaffService {
    List<DocumentDto> getDocumentsByRenterId(Long userId);
    DocumentDto verifyDocument(Long documentId);
}
