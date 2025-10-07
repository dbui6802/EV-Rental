package com.webserver.evrentalsystem.service.staff.impl;

import com.webserver.evrentalsystem.model.dto.entitydto.DocumentDto;
import com.webserver.evrentalsystem.model.mapping.DocumentMapper;
import com.webserver.evrentalsystem.repository.DocumentRepository;
import com.webserver.evrentalsystem.service.staff.RenterDocumentStaffService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RenterDocumentStaffServiceImpl implements RenterDocumentStaffService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private UserValidation userValidation;

    @Override
    public List<DocumentDto> getDocumentsByRenterId(Long renterId) {
        userValidation.validateStaff();
        if (renterId == null || renterId <= 0) {
            throw new IllegalArgumentException("Renter ID không hợp lệ");
        }
        return documentRepository.findByUserId(renterId)
                .stream()
                .map(documentMapper::toDocumentDto)
                .collect(Collectors.toList());
    }
}
