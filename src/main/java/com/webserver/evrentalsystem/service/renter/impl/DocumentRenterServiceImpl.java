package com.webserver.evrentalsystem.service.renter.impl;

import com.webserver.evrentalsystem.entity.Document;
import com.webserver.evrentalsystem.entity.DocumentType;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.PermissionDeniedException;
import com.webserver.evrentalsystem.model.dto.entitydto.DocumentDto;
import com.webserver.evrentalsystem.model.mapping.DocumentMapper;
import com.webserver.evrentalsystem.repository.DocumentRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.renter.DocumentRenterService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DocumentRenterServiceImpl implements DocumentRenterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private DocumentMapper documentMapper;

    @Override
    public DocumentDto uploadDocument(String type, String documentNumber, MultipartFile file) {
        User user = userValidation.validateUser();

        // Check document type is valid
        try {
            DocumentType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new InvalidateParamsException("Loại tài liệu không hợp lệ: " + type);
        }

        // File must be not empty
        if (file.isEmpty()) {
            throw new InvalidateParamsException("File tài liệu không được rỗng");
        }

        // Limit file size to 5MB
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new InvalidateParamsException("Kích thước file vượt quá giới hạn 5MB");
        }

        // Only accept JPG, PNG, PDF
        String contentType = file.getContentType();
        if (contentType == null || !(
                contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("application/pdf")
        )) {
            throw new InvalidateParamsException("Định dạng file không hợp lệ. Chỉ chấp nhận JPG, PNG, PDF");
        }

        // Document number must be not empty
        if (documentNumber == null || documentNumber.trim().isEmpty()) {
            throw new InvalidateParamsException("Số tài liệu không được rỗng");
        }

        // Create upload directory if not exists
        Path uploadDir = Paths.get("uploads");
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Random filename to avoid conflicts
            String originalFilename = file.getOriginalFilename();
            String filename = UUID.randomUUID() + "_" + (originalFilename != null ? originalFilename : "file");
            Path filePath = uploadDir.resolve(filename);

            // Save file to disk
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // File URL (assuming served from /uploads/)
            String fileUrl = "/uploads/" + filename;

            // Tạo Document entity
            Document doc = new Document();
            doc.setUser(user);
            doc.setType(DocumentType.valueOf(type));
            doc.setDocumentNumber(documentNumber);
            doc.setDocumentUrl(fileUrl);
            doc.setVerified(false);
            doc.setCreatedAt(LocalDateTime.now());

            Document saved = documentRepository.save(doc);
            return documentMapper.toDocumentDto(saved);

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu file", e);
        }
    }


    @Override
    public List<DocumentDto> getDocuments() {
        User user = userValidation.validateUser();
        return documentRepository.findByUserId(user.getId()).stream()
                .map(documentMapper::toDocumentDto)
                .toList();
    }

    @Override
    public void deleteDocument(Long id) {
        User user = userValidation.validateUser();
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy tài liệu với ID: " + id));
        if (!doc.getUser().getId().equals(user.getId())) {
            throw new PermissionDeniedException("Bạn không có quyền xoá tài liệu này");
        }
        documentRepository.delete(doc);
    }
}
