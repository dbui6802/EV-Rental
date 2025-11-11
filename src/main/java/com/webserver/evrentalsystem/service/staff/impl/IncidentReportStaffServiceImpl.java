package com.webserver.evrentalsystem.service.staff.impl;

import com.webserver.evrentalsystem.entity.*;
import com.webserver.evrentalsystem.exception.ConflictException;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.model.dto.entitydto.IncidentReportDto;
import com.webserver.evrentalsystem.model.dto.request.IncidentReportRequest;
import com.webserver.evrentalsystem.model.mapping.IncidentReportMapper;
import com.webserver.evrentalsystem.repository.IncidentReportRepository;
import com.webserver.evrentalsystem.repository.RentalRepository;
import com.webserver.evrentalsystem.repository.VehicleRepository;
import com.webserver.evrentalsystem.repository.StaffStationRepository;
import com.webserver.evrentalsystem.service.staff.IncidentReportStaffService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IncidentReportStaffServiceImpl implements IncidentReportStaffService {

    @Autowired
    private IncidentReportRepository incidentReportRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private IncidentReportMapper incidentReportMapper;

    @Autowired
    private StaffStationRepository staffStationRepository;

    @Override
    public IncidentReportDto createIncidentReport(IncidentReportRequest request) {
        User staff = userValidation.validateStaff();

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy xe"));


        boolean hasExistingIncident = incidentReportRepository.existsByVehicleAndStatusIn(
                vehicle,
                List.of(IncidentStatus.PENDING, IncidentStatus.IN_REVIEW)
        );

        if (hasExistingIncident) {
            throw new ConflictException("Đã tồn tại một báo cáo sự cố cho xe này đang ở trạng thái chờ xử lý hoặc đang xem xét.");
        }

        vehicle.setStatus(VehicleStatus.MAINTENANCE);
        vehicleRepository.save(vehicle);

        Rental rental = null;
        if (request.getRentalId() != null) {
            rental = rentalRepository.findById(request.getRentalId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy lượt thuê"));
        }

        IncidentReport report = new IncidentReport();
        report.setVehicle(vehicle);
        report.setRental(rental);
        report.setStaff(staff);
        report.setDescription(request.getDescription());
        report.setSeverity(request.getSeverity());
        report.setStatus(IncidentStatus.PENDING);
        report.setCreatedAt(LocalDateTime.now());

        report = incidentReportRepository.save(report);
        return incidentReportMapper.toIncidentReportDto(report);
    }

    @Override
    public List<IncidentReportDto> getAllIncidents(String status) {
        User staff = userValidation.validateStaff();
        StaffStation activeAssignment = staffStationRepository.findByStaffIdAndIsActiveTrue(staff.getId());

        if (activeAssignment == null) {
            return Collections.emptyList();
        }
        Long stationId = activeAssignment.getStation().getId();

        Specification<IncidentReport> spec = (root, query, cb) -> {
            return cb.equal(root.get("vehicle").get("station").get("id"), stationId);
        };

        if (status != null && !status.isBlank()) {
            try {
                IncidentStatus incidentStatus = IncidentStatus.valueOf(status.toUpperCase());
                spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), incidentStatus));
            } catch (IllegalArgumentException e) {
                throw new InvalidateParamsException("Giá trị trạng thái sự cố không hợp lệ: " + status);
            }
        }

        List<IncidentReport> incidents = incidentReportRepository.findAll(spec);

        return incidents.stream()
                .map(incidentReportMapper::toIncidentReportDto)
                .collect(Collectors.toList());
    }

    @Override
    public IncidentReportDto getIncidentById(Long id) {
        IncidentReport incidentReport = incidentReportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy báo cáo sự cố với ID: " + id));
        return incidentReportMapper.toIncidentReportDto(incidentReport);
    }
}
