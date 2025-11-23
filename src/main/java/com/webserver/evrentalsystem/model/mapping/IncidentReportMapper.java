package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.IncidentReport;
import com.webserver.evrentalsystem.model.dto.entitydto.IncidentReportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, RentalMapper.class, VehicleMapper.class})
public interface IncidentReportMapper {
    @Mapping(source = "status.value", target = "status")
    @Mapping(source = "rental.renter", target = "renter")
    @Mapping(source = "rental.id", target = "rentalId")
    IncidentReportDto toIncidentReportDto(IncidentReport incidentReport);

    List<IncidentReportDto> toIncidentReportDtoList(List<IncidentReport> incidentReports);
}
