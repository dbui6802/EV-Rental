package com.webserver.evrentalsystem.service.staff.impl;

import com.webserver.evrentalsystem.entity.*;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.model.dto.entitydto.IncidentReportDto;
import com.webserver.evrentalsystem.model.dto.request.IncidentReportRequest;
import com.webserver.evrentalsystem.model.mapping.IncidentReportMapper;
import com.webserver.evrentalsystem.repository.IncidentReportRepository;
import com.webserver.evrentalsystem.repository.RentalRepository;
import com.webserver.evrentalsystem.repository.VehicleRepository;
import com.webserver.evrentalsystem.service.staff.IncidentReportStaffService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Override
    public IncidentReportDto createIncidentReport(IncidentReportRequest request) {
        User staff = userValidation.validateStaff();

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));

        Rental rental = null;
        if (request.getRentalId() != null) {
            rental = rentalRepository.findById(request.getRentalId())
                    .orElseThrow(() -> new NotFoundException("Rental not found"));
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
}
