package com.webserver.evrentalsystem.service.admin.impl;

import com.webserver.evrentalsystem.entity.*;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.model.dto.request.CreateVehicleRequest;
import com.webserver.evrentalsystem.model.dto.request.UpdateVehicleRequest;
import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;
import com.webserver.evrentalsystem.model.mapping.VehicleMapper;
import com.webserver.evrentalsystem.repository.StationRepository;
import com.webserver.evrentalsystem.repository.VehicleRepository;
import com.webserver.evrentalsystem.service.admin.VehicleAdminService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class VehicleAdminServiceImpl implements VehicleAdminService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private UserValidation userValidation;

    @Override
    public VehicleDto createVehicle(CreateVehicleRequest request) {
        userValidation.validateAdmin();
        if (request.getLicensePlate() == null || request.getType() == null ||
                request.getBrand() == null || request.getModel() == null || request.getCapacity() == null ||
                request.getRangePerFullCharge() == null || request.getPricePerHour() == null ||
                request.getStationId() == null) {
            throw new InvalidateParamsException("Thiếu thông tin bắt buộc khi tạo xe");
        }

        if (vehicleRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new InvalidateParamsException("Biển số xe đã tồn tại");
        }

        VehicleType type = VehicleType.fromValue(request.getType());
        VehicleStatus status = VehicleStatus.fromValue(request.getStatus());
        if (type == null) throw new InvalidateParamsException("Loại xe không hợp lệ");
        if (status == null) status = VehicleStatus.AVAILABLE; // default

        Station station = stationRepository.findById(request.getStationId())
                .orElseThrow(() -> new InvalidateParamsException("Không tìm thấy trạm với id = " + request.getStationId()));

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setType(type);
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setCapacity(request.getCapacity());
        vehicle.setRangePerFullCharge(request.getRangePerFullCharge());
        vehicle.setStatus(status);
        vehicle.setPricePerHour(request.getPricePerHour());
        vehicle.setStation(station);

        return vehicleMapper.toVehicleDto(vehicleRepository.save(vehicle));
    }

    @Override
    public List<VehicleDto> getAllVehicles(String status, String plateNumber) {
        userValidation.validateAdmin();
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

    @Override
    public VehicleDto getVehicleById(Long id) {
        userValidation.validateAdmin();
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy xe với id = " + id));
        return vehicleMapper.toVehicleDto(vehicle);
    }

    @Override
    public VehicleDto updateVehicle(Long id, UpdateVehicleRequest request) {
        userValidation.validateAdmin();
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy xe với id = " + id));

        if (request.getLicensePlate() != null) {
            if (!request.getLicensePlate().equalsIgnoreCase(vehicle.getLicensePlate()) &&
                    vehicleRepository.existsByLicensePlate(request.getLicensePlate())) {
                throw new InvalidateParamsException("Biển số xe đã tồn tại");
            }
            vehicle.setLicensePlate(request.getLicensePlate());
        }

        if (request.getType() != null) {
            VehicleType type = VehicleType.fromValue(request.getType());
            if (type == null) throw new InvalidateParamsException("Loại xe không hợp lệ");
            vehicle.setType(type);
        }

        if (request.getBrand() != null) vehicle.setBrand(request.getBrand());
        if (request.getModel() != null) vehicle.setModel(request.getModel());
        if (request.getCapacity() != null) vehicle.setCapacity(request.getCapacity());
        if (request.getRangePerFullCharge() != null) vehicle.setRangePerFullCharge(request.getRangePerFullCharge());

        if (request.getStatus() != null) {
            VehicleStatus status = VehicleStatus.fromValue(request.getStatus());
            if (status == null) throw new InvalidateParamsException("Trạng thái không hợp lệ");
            vehicle.setStatus(status);
        }

        if (request.getPricePerHour() != null) vehicle.setPricePerHour(request.getPricePerHour());

        if (request.getStationId() != null) {
            Station station = stationRepository.findById(request.getStationId())
                    .orElseThrow(() -> new InvalidateParamsException("Không tìm thấy trạm với id = " + request.getStationId()));
            vehicle.setStation(station);
        }

        return vehicleMapper.toVehicleDto(vehicleRepository.save(vehicle));
    }

    @Override
    public void deleteVehicle(Long id) {
        userValidation.validateAdmin();
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy xe với id = " + id));
        vehicleRepository.delete(vehicle);
    }

    @Override
    public Map<String, Long> getVehicleStatusStatistics() {
        userValidation.validateAdmin();
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getStatus().getValue(),
                        Collectors.counting()
                ));
    }
}
