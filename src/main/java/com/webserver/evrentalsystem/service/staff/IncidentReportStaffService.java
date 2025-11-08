package com.webserver.evrentalsystem.service.staff;

import com.webserver.evrentalsystem.model.dto.entitydto.IncidentReportDto;
import com.webserver.evrentalsystem.model.dto.request.IncidentReportRequest;

import java.util.List;

public interface IncidentReportStaffService {
    IncidentReportDto createIncidentReport(IncidentReportRequest request);
    List<IncidentReportDto> getAllIncidents(String status);
    IncidentReportDto getIncidentById(Long id);
}
