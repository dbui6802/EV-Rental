package com.webserver.evrentalsystem.service.staff.impl;

import com.webserver.evrentalsystem.entity.Role;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.model.dto.entitydto.DocumentDto;
import com.webserver.evrentalsystem.model.dto.entitydto.UserDto;
import com.webserver.evrentalsystem.model.mapping.DocumentMapper;
import com.webserver.evrentalsystem.model.mapping.UserMapper;
import com.webserver.evrentalsystem.repository.DocumentRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers(String phone) {
        userValidation.validateStaff();
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.RENTER)
                .filter(u -> (phone == null || u.getPhone().contains(phone)))
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

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

    @Override
    public DocumentDto verifyDocument(Long documentId) {
        User staff = userValidation.validateStaff();
        if (documentId == null || documentId <= 0) {
            throw new IllegalArgumentException("Document ID không hợp lệ");
        }
        var document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài liệu với ID: " + documentId));
        document.setVerified(true);
        document.setVerifiedBy(staff);
        var updatedDocument = documentRepository.save(document);
        return documentMapper.toDocumentDto(updatedDocument);
    }
}
