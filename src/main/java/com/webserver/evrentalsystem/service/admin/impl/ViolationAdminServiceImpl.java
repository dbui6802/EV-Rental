package com.webserver.evrentalsystem.service.admin.impl;

import com.webserver.evrentalsystem.entity.Violation;
import com.webserver.evrentalsystem.model.dto.entitydto.ViolationDto;
import com.webserver.evrentalsystem.model.mapping.ViolationMapper;
import com.webserver.evrentalsystem.repository.ViolationRepository;
import com.webserver.evrentalsystem.service.admin.ViolationAdminService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ViolationAdminServiceImpl implements ViolationAdminService {

    @Autowired
    private ViolationRepository violationRepository;

    @Autowired
    private ViolationMapper violationMapper;

    @Override
    public List<ViolationDto> getViolations(Long renterId, Long rentalId) {
        List<Violation> violations;

        if (rentalId != null) {
            violations = violationRepository.findByRentalId(rentalId);
        } else if (renterId != null) {
            violations = violationRepository.findByRentalRenterId(renterId);
        } else {
            violations = violationRepository.findAll();
        }

        return violations.stream()
                .map(violationMapper::toViolationDto)
                .toList();
    }
}
