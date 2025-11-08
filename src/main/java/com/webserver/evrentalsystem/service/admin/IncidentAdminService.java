package com.webserver.evrentalsystem.service.admin;

import com.webserver.evrentalsystem.model.dto.entitydto.IncidentReportDto;
import com.webserver.evrentalsystem.model.dto.request.ResolveIncidentRequest;

import java.util.List;

public interface IncidentAdminService {
    IncidentReportDto resolveIncident(ResolveIncidentRequest request);
    List<IncidentReportDto> getAllIncidents(String status);
}
