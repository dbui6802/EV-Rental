package com.webserver.evrentalsystem.service.staff;

import com.webserver.evrentalsystem.model.dto.entitydto.IncidentReportDto;
import com.webserver.evrentalsystem.model.dto.request.IncidentReportRequest;

public interface IncidentReportStaffService {
    IncidentReportDto createIncidentReport(IncidentReportRequest request);
}
