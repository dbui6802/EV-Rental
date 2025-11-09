package com.webserver.evrentalsystem.service.admin.impl;

import com.webserver.evrentalsystem.entity.Complaint;
import com.webserver.evrentalsystem.entity.ComplaintStatus;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.model.dto.entitydto.ComplaintDto;
import com.webserver.evrentalsystem.model.dto.request.ResolveComplaintRequest;
import com.webserver.evrentalsystem.model.mapping.ComplaintMapper;
import com.webserver.evrentalsystem.repository.ComplaintRepository;
import com.webserver.evrentalsystem.service.admin.ComplaintAdminService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ComplaintAdminServiceImpl implements ComplaintAdminService {
    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ComplaintMapper complaintMapper;

    @Autowired
    private UserValidation userValidation;

    @Override
    public List<ComplaintDto> getAllComplaints(String status) {
        userValidation.validateAdmin();

        List<Complaint> complaints;
        if (status != null) {
            complaints = complaintRepository.findByStatus(ComplaintStatus.fromValue(status));
        } else {
            complaints = complaintRepository.findAll();
        }
        return complaints.stream()
                .map(complaintMapper::toComplaintDto)
                .toList();
    }

    @Override
    public ComplaintDto resolveComplaint(ResolveComplaintRequest request) {
        User admin = userValidation.validateAdmin();

        Complaint complaint = complaintRepository.findById(request.getComplaintId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khiếu nại"));

        ComplaintStatus newStatus = ComplaintStatus.fromValue(request.getStatus());
        if (newStatus == null || newStatus == ComplaintStatus.PENDING) {
            throw new InvalidateParamsException("Trạng thái không hợp lệ. Chỉ cho phép 'resolved' hoặc 'rejected'");
        }

        if (request.getResolution() == null || request.getResolution().isBlank()) {
            throw new InvalidateParamsException("Ghi chú xử lý là bắt buộc.");
        }

        complaint.setStatus(newStatus);
        complaint.setAdmin(admin);
        complaint.setResolution(request.getResolution());
        complaint.setResolvedAt(LocalDateTime.now());

        return complaintMapper.toComplaintDto(complaintRepository.save(complaint));
    }
}
