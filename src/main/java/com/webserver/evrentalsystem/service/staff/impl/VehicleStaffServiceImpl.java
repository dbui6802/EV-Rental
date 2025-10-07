package com.webserver.evrentalsystem.service.staff.impl;

import com.webserver.evrentalsystem.entity.Vehicle;
import com.webserver.evrentalsystem.entity.VehicleStatus;
import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;
import com.webserver.evrentalsystem.model.mapping.VehicleMapper;
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
    private VehicleMapper vehicleMapper;

    @Autowired
    private UserValidation userValidation;

    @Override
    public List<VehicleDto> getAllVehicles(String status, String plateNumber) {
        userValidation.validateStaff();

        List<Vehicle> vehicles = vehicleRepository.findAll();

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
}
