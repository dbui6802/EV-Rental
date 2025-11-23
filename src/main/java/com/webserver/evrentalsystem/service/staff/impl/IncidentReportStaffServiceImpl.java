package com.webserver.evrentalsystem.service.staff.impl;

import com.webserver.evrentalsystem.entity.*;
import com.webserver.evrentalsystem.exception.BadRequestException;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.model.dto.entitydto.IncidentReportDto;
import com.webserver.evrentalsystem.model.dto.request.IncidentReportUpdateRequest;
import com.webserver.evrentalsystem.model.mapping.IncidentReportMapper;
import com.webserver.evrentalsystem.repository.IncidentReportRepository;
import com.webserver.evrentalsystem.repository.RentalRepository;
import com.webserver.evrentalsystem.repository.VehicleRepository;
import com.webserver.evrentalsystem.service.staff.IncidentReportStaffService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IncidentReportStaffServiceImpl implements IncidentReportStaffService {

    @Autowired
    private IncidentReportRepository incidentReportRepository;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private IncidentReportMapper incidentReportMapper;
    @Autowired
    private UserValidation userValidation;

    @Override
    public List<IncidentReportDto> getAllIncidents(String status) {
        userValidation.validateStaff();
        List<IncidentReport> incidentReports;
        if (status != null && !status.isEmpty()) {
            try {
                IncidentStatus incidentStatus = IncidentStatus.valueOf(status.toUpperCase());
                incidentReports = incidentReportRepository.findByStatus(incidentStatus);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Trạng thái không hợp lệ: " + status);
            }
        } else {
            incidentReports = incidentReportRepository.findAll();
        }
        return incidentReportMapper.toIncidentReportDtoList(incidentReports);
    }

    @Override
    public IncidentReportDto getIncidentById(Long id) {
        userValidation.validateStaff();
        IncidentReport incidentReport = incidentReportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy báo cáo sự cố với ID: " + id));
        return incidentReportMapper.toIncidentReportDto(incidentReport);
    }


    @Override
    @Transactional
    public IncidentReportDto updateIncidentReport(Long id, IncidentReportUpdateRequest request) {
        User staff = userValidation.validateStaff();

        IncidentReport incidentReport = incidentReportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy báo cáo sự cố với ID: " + id));

        // Chỉ xử lý logic khi sự cố được giải quyết
        if (request.getStatus() == IncidentStatus.RESOLVED) {
            // Ghi chú xử lý là bắt buộc
            if (request.getResolutionNotes() == null || request.getResolutionNotes().isBlank()) {
                throw new BadRequestException("Ghi chú xử lý là bắt buộc khi chuyển trạng thái thành RESOLVED.");
            }

            // ID xe mới là bắt buộc
            if (request.getNewVehicleId() == null) {
                throw new BadRequestException("ID của xe mới là bắt buộc để giải quyết sự cố và đổi xe.");
            }

            Rental rental = incidentReport.getRental();
            Vehicle oldVehicle = incidentReport.getVehicle();

            // 1. Xe cũ chuyển sang trạng thái chờ kiểm tra
            oldVehicle.setStatus(VehicleStatus.AWAITING_INSPECTION);
            vehicleRepository.save(oldVehicle);

            // 2. Lấy và kiểm tra xe mới
            Vehicle newVehicle = vehicleRepository.findById(request.getNewVehicleId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy xe mới với ID: " + request.getNewVehicleId()));

            if (newVehicle.getStatus() != VehicleStatus.AVAILABLE) {
                throw new BadRequestException("Xe mới không sẵn sàng để cho thuê.");
            }

            // 3. Cập nhật hợp đồng thuê để trỏ đến xe mới
            rental.setVehicle(newVehicle);
            rentalRepository.save(rental);

            // 4. Cập nhật trạng thái xe mới
            newVehicle.setStatus(VehicleStatus.RENTED);
            vehicleRepository.save(newVehicle);
        }

        // Cập nhật thông tin báo cáo sự cố
        incidentReport.setStatus(request.getStatus());
        incidentReport.setResolutionNotes(request.getResolutionNotes());
        incidentReport.setStaff(staff);

        IncidentReport updatedIncident = incidentReportRepository.save(incidentReport);
        return incidentReportMapper.toIncidentReportDto(updatedIncident);
    }
}
