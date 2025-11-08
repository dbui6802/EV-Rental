package com.webserver.evrentalsystem.service.admin.impl;

import com.webserver.evrentalsystem.entity.IncidentReport;
import com.webserver.evrentalsystem.entity.IncidentStatus;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.entity.Vehicle;
import com.webserver.evrentalsystem.entity.VehicleStatus;
import com.webserver.evrentalsystem.exception.ConflictException;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.model.dto.entitydto.IncidentReportDto;
import com.webserver.evrentalsystem.model.dto.request.ResolveIncidentRequest;
import com.webserver.evrentalsystem.model.mapping.IncidentReportMapper;
import com.webserver.evrentalsystem.repository.IncidentReportRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.repository.VehicleRepository;
import com.webserver.evrentalsystem.service.admin.IncidentAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IncidentAdminServiceImpl implements IncidentAdminService {

    @Autowired
    private IncidentReportRepository incidentReportRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IncidentReportMapper incidentReportMapper;

    @Override
    public IncidentReportDto resolveIncident(ResolveIncidentRequest request) {
        IncidentReport incidentReport = incidentReportRepository.findById(request.getIncidentId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy báo cáo sự cố với ID: " + request.getIncidentId()));

        if (incidentReport.getStatus() == IncidentStatus.RESOLVED) {
            throw new ConflictException("Sự cố này đã được giải quyết.");
        }

        String adminPhone = SecurityContextHolder.getContext().getAuthentication().getName();
        User admin = userRepository.findByPhone(adminPhone);
        if (admin == null) {
            throw new NotFoundException("Không tìm thấy quản trị viên với số điện thoại: " + adminPhone);
        }

        Vehicle vehicle = incidentReport.getVehicle();
        if (vehicle == null) {
            throw new NotFoundException("Không tìm thấy xe cho sự cố này.");
        }

        IncidentStatus newStatus;
        try {
            newStatus = IncidentStatus.valueOf(request.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidateParamsException("Trạng thái sự cố không hợp lệ: " + request.getStatus());
        }


        incidentReport.setAdmin(admin);
        incidentReport.setStatus(newStatus);

        if (newStatus == IncidentStatus.RESOLVED) {
            if (request.getResolutionNotes() == null || request.getResolutionNotes().isBlank()) {
                throw new InvalidateParamsException("Ghi chú xử lý là bắt buộc khi giải quyết sự cố.");
            }
            incidentReport.setResolutionNotes(request.getResolutionNotes());
            incidentReport.setResolvedAt(LocalDateTime.now());
            vehicle.setStatus(VehicleStatus.AWAITING_INSPECTION);
        }

        incidentReportRepository.save(incidentReport);
        vehicleRepository.save(vehicle);

        return incidentReportMapper.toIncidentReportDto(incidentReport);
    }

    @Override
    public List<IncidentReportDto> getAllIncidents(String status) {
        List<IncidentReport> incidents = incidentReportRepository.findAll();

        if (status != null && !status.isBlank()) {
            try {
                IncidentStatus incidentStatus = IncidentStatus.valueOf(status.toUpperCase());
                incidents = incidents.stream()
                        .filter(i -> i.getStatus() == incidentStatus)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new InvalidateParamsException("Giá trị trạng thái sự cố không hợp lệ: " + status);
            }
        }

        return incidents.stream()
                .map(incidentReportMapper::toIncidentReportDto)
                .collect(Collectors.toList());
    }
}
