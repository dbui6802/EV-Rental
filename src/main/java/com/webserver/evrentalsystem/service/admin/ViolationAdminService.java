package com.webserver.evrentalsystem.service.admin;

import com.webserver.evrentalsystem.model.dto.entitydto.ViolationDto;

import java.util.List;

public interface ViolationAdminService {
    List<ViolationDto> getViolations(Long renterId, Long rentalId);
}
