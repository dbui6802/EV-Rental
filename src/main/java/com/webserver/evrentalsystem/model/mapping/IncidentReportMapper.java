package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.IncidentReport;
import com.webserver.evrentalsystem.model.dto.entitydto.IncidentReportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, RentalMapper.class, VehicleMapper.class})
public interface IncidentReportMapper {
    @Mapping(source = "severity.value", target = "severity") // enum IncidentSeverity -> string
    @Mapping(source = "status.value", target = "status") // enum IncidentStatus -> string
    IncidentReportDto toIncidentReportDto(IncidentReport incidentReport);
}
