package com.webserver.evrentalsystem.service.staff.impl;

import com.webserver.evrentalsystem.entity.*;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;
import com.webserver.evrentalsystem.model.dto.request.StaffUpdateVehicleRequest;
import com.webserver.evrentalsystem.model.mapping.VehicleMapper;
import com.webserver.evrentalsystem.repository.StaffStationRepository;
import com.webserver.evrentalsystem.repository.StationRepository;
import com.webserver.evrentalsystem.repository.VehicleRepository;
import com.webserver.evrentalsystem.service.staff.VehicleStaffService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VehicleStaffServiceImpl implements VehicleStaffService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private StaffStationRepository staffStationRepository;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private UserValidation userValidation;

    @Override
    public List<VehicleDto> getAllVehicles(String status, String plateNumber) {
        User staff = userValidation.validateStaff();
        Station station = staffStationRepository.findAllByStaffId(staff.getId()).stream()
                .findFirst()
                .orElseThrow(() -> new InvalidateParamsException("Nhân viên chưa được phân công trạm"))
                .getStation();

        List<Vehicle> vehicles = vehicleRepository.findByStationId(station.getId());

        if (status != null) {
            VehicleStatus s = VehicleStatus.fromValue(status);
            if (s != null) {
                vehicles = vehicles.stream()
                        .filter(v -> v.getStatus() == s)
                        .collect(Collectors.toList());
            }
        }

        if (plateNumber != null && !plateNumber.isBlank()) {
            vehicles = vehicles.stream()
                    .filter(v -> v.getLicensePlate().equalsIgnoreCase(plateNumber))
                    .collect(Collectors.toList());
        }

        return vehicles.stream().map(vehicleMapper::toVehicleDto).toList();
    }

    @Override
    public VehicleDto updateVehicle(Long id, StaffUpdateVehicleRequest request) {
        User staff = userValidation.validateStaff();
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy xe với id = " + id));

        staffStationRepository.findAllByStaffId(staff.getId()).stream()
                .filter(s -> s.getStation().getId().equals(vehicle.getStation().getId()))
                .findFirst()
                .orElseThrow(() -> new InvalidateParamsException("Nhân viên không thuộc trạm có xe này"));

        if (request.getBrand() != null) vehicle.setBrand(request.getBrand());
        if (request.getModel() != null) vehicle.setModel(request.getModel());
        if (request.getCapacity() != null) vehicle.setCapacity(request.getCapacity());
        if (request.getRangePerFullCharge() != null) vehicle.setRangePerFullCharge(request.getRangePerFullCharge());

        return vehicleMapper.toVehicleDto(vehicleRepository.save(vehicle));
    }
}
