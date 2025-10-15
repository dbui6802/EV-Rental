package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.StaffStation;
import com.webserver.evrentalsystem.model.dto.entitydto.StaffStationDto;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-15T08:53:04+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
)
@Component
public class StaffStationMapperImpl implements StaffStationMapper {

    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public StaffStationDto toStaffStationDto(StaffStation staffStation) {
        if ( staffStation == null ) {
            return null;
        }

        StaffStationDto.StaffStationDtoBuilder staffStationDto = StaffStationDto.builder();

        staffStationDto.id( staffStation.getId() );
        staffStationDto.staff( userMapper.toUserDto( staffStation.getStaff() ) );
        staffStationDto.station( stationMapper.toStationDto( staffStation.getStation() ) );
        staffStationDto.assignedAt( staffStation.getAssignedAt() );
        staffStationDto.deactivatedAt( staffStation.getDeactivatedAt() );
        staffStationDto.isActive( staffStation.getIsActive() );

        return staffStationDto.build();
    }
}
