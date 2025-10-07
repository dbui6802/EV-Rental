package com.webserver.evrentalsystem.service.admin;

import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;
import com.webserver.evrentalsystem.model.dto.request.CreateVehicleRequest;
import com.webserver.evrentalsystem.model.dto.request.UpdateVehicleRequest;

import java.util.List;
import java.util.Map;

public interface VehicleAdminService {
    VehicleDto createVehicle(CreateVehicleRequest request);
    List<VehicleDto> getAllVehicles(String status, String plateNumber);
    VehicleDto getVehicleById(Long id);
    VehicleDto updateVehicle(Long id, UpdateVehicleRequest request);
    void deleteVehicle(Long id);
    Map<String, Long> getVehicleStatusStatistics();
}
