package com.webserver.evrentalsystem.service.staff;

import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;

import java.util.List;

public interface VehicleStaffService {
    List<VehicleDto> getAllVehicles(String status, String plateNumber);
}
