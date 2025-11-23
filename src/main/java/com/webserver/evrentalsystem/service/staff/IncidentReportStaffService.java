package com.webserver.evrentalsystem.service.staff;

import com.webserver.evrentalsystem.model.dto.entitydto.IncidentReportDto;
import com.webserver.evrentalsystem.model.dto.request.IncidentReportUpdateRequest;

import java.util.List;

public interface IncidentReportStaffService {
    List<IncidentReportDto> getAllIncidents(String status);
    IncidentReportDto getIncidentById(Long id);
    IncidentReportDto updateIncidentReport(Long id, IncidentReportUpdateRequest request);
}
