package com.webserver.evrentalsystem.service.renter;

import com.webserver.evrentalsystem.model.dto.entitydto.DocumentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentRenterService {
    DocumentDto uploadDocument(String type, String documentNumber, MultipartFile file);

    List<DocumentDto> getDocuments();

    void deleteDocument(Long id);
}
