package com.webserver.evrentalsystem.service.staff;

import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;
import com.webserver.evrentalsystem.model.dto.request.StaffUpdateVehicleRequest;

import java.util.List;

public interface VehicleStaffService {
    List<VehicleDto> getAllVehicles(String status, String plateNumber);
    VehicleDto updateVehicle(Long id, StaffUpdateVehicleRequest request);
}
