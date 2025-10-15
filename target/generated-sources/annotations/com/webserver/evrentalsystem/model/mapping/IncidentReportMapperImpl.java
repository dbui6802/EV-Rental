package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.IncidentReport;
import com.webserver.evrentalsystem.entity.IncidentSeverity;
import com.webserver.evrentalsystem.entity.IncidentStatus;
import com.webserver.evrentalsystem.model.dto.entitydto.IncidentReportDto;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-15T10:58:28+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
)
@Component
public class IncidentReportMapperImpl implements IncidentReportMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RentalMapper rentalMapper;
    @Autowired
    private VehicleMapper vehicleMapper;

    @Override
    public IncidentReportDto toIncidentReportDto(IncidentReport incidentReport) {
        if ( incidentReport == null ) {
            return null;
        }

        IncidentReportDto.IncidentReportDtoBuilder incidentReportDto = IncidentReportDto.builder();

        incidentReportDto.severity( incidentReportSeverityValue( incidentReport ) );
        incidentReportDto.status( incidentReportStatusValue( incidentReport ) );
        incidentReportDto.id( incidentReport.getId() );
        incidentReportDto.vehicle( vehicleMapper.toVehicleDto( incidentReport.getVehicle() ) );
        incidentReportDto.rental( rentalMapper.toRentalDto( incidentReport.getRental() ) );
        incidentReportDto.staff( userMapper.toUserDto( incidentReport.getStaff() ) );
        incidentReportDto.description( incidentReport.getDescription() );
        incidentReportDto.createdAt( incidentReport.getCreatedAt() );

        return incidentReportDto.build();
    }

    private String incidentReportSeverityValue(IncidentReport incidentReport) {
        if ( incidentReport == null ) {
            return null;
        }
        IncidentSeverity severity = incidentReport.getSeverity();
        if ( severity == null ) {
            return null;
        }
        String value = severity.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }

    private String incidentReportStatusValue(IncidentReport incidentReport) {
        if ( incidentReport == null ) {
            return null;
        }
        IncidentStatus status = incidentReport.getStatus();
        if ( status == null ) {
            return null;
        }
        String value = status.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }
}
