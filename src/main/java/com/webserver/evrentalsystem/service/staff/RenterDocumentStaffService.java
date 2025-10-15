package com.webserver.evrentalsystem.service.staff;

import com.webserver.evrentalsystem.model.dto.entitydto.DocumentDto;
import com.webserver.evrentalsystem.model.dto.entitydto.UserDto;

import java.util.List;

public interface RenterDocumentStaffService {
    List<UserDto> getAllUsers(String phone);
    List<DocumentDto> getDocumentsByRenterId(Long userId);
    DocumentDto verifyDocument(Long documentId);
}
