package com.webserver.evrentalsystem.service.global;

import com.webserver.evrentalsystem.model.dto.entitydto.StationDto;
import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;

import java.util.List;

public interface PublicService {
    List<StationDto> getStations();
    List<VehicleDto> getVehicles(String type, Long stationId, Double priceMin, Double priceMax);
}
