package com.webserver.evrentalsystem.service.renter.impl;

import com.webserver.evrentalsystem.entity.Complaint;
import com.webserver.evrentalsystem.entity.ComplaintStatus;
import com.webserver.evrentalsystem.entity.Rental;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.model.dto.entitydto.ComplaintDto;
import com.webserver.evrentalsystem.model.dto.request.ComplaintRequest;
import com.webserver.evrentalsystem.model.mapping.ComplaintMapper;
import com.webserver.evrentalsystem.repository.ComplaintRepository;
import com.webserver.evrentalsystem.repository.RentalRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.renter.ComplaintRenterService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ComplaintRenterServiceImpl implements ComplaintRenterService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ComplaintMapper complaintMapper;

    @Autowired
    private UserValidation userValidation;

    @Override
    public ComplaintDto createComplaint(ComplaintRequest request) {
        User renter = userValidation.validateRenter();

        Rental rental = rentalRepository.findById(request.getRentalId())
                .orElseThrow(() -> new NotFoundException("Rental not found"));

        User staff = null;
        if (request.getStaffId() != null) {
            staff = userRepository.findById(request.getStaffId())
                    .orElseThrow(() -> new NotFoundException("Staff not found"));
        }

        Complaint complaint = new Complaint();
        complaint.setRental(rental);
        complaint.setRenter(renter);
        complaint.setStaff(staff);
        complaint.setDescription(request.getDescription());
        complaint.setStatus(ComplaintStatus.PENDING);
        complaint.setCreatedAt(LocalDateTime.now());

        return complaintMapper.toComplaintDto(complaintRepository.save(complaint));
    }

    @Override
    public List<ComplaintDto> getComplaintsOfRenter() {
        User renter = userValidation.validateRenter();

        List<Complaint> complaints = complaintRepository.findByRenterId(renter.getId());
        return complaints.stream()
                .map(complaintMapper::toComplaintDto)
                .toList();
    }

    @Override
    public ComplaintDto getComplaintDetail(Long complaintId) {
        User renter = userValidation.validateRenter();

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khiếu nại với id = " + complaintId));

        // Chỉ cho phép renter xem khiếu nại của chính họ
        if (!complaint.getRenter().getId().equals(renter.getId())) {
            throw new NotFoundException("Bạn không có quyền xem khiếu nại này");
        }

        return complaintMapper.toComplaintDto(complaint);
    }
}
