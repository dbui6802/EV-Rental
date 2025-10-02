package com.webserver.evrentalsystem.service.admin;

import com.webserver.evrentalsystem.model.dto.entitydto.StaffStationDto;
import com.webserver.evrentalsystem.model.dto.request.AssignStaffRequest;

import java.util.List;

public interface StaffStationAdminService {
    StaffStationDto assignStaffToStation(AssignStaffRequest request);
    StaffStationDto deactivateAssignment(Long id);
    List<StaffStationDto> listAssignments(Long stationId);
}
