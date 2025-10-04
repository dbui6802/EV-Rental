package com.webserver.evrentalsystem.service.admin;

import com.webserver.evrentalsystem.model.dto.entitydto.StationDto;
import com.webserver.evrentalsystem.model.dto.request.CreateStationRequest;
import com.webserver.evrentalsystem.model.dto.request.UpdateStationRequest;

import java.util.List;

public interface StationAdminService {
    StationDto createStation(CreateStationRequest request);

    List<StationDto> getAllStations();

    StationDto getStationById(Long id);

    StationDto updateStation(Long id, UpdateStationRequest request);

    void deleteStation(Long id);
}
