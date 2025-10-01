package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.StaffStation;
import com.webserver.evrentalsystem.model.dto.entitydto.StaffStationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StationMapper.class, UserMapper.class})
public interface StaffStationMapper {
    StaffStationDto toStaffStationDto(StaffStation staffStation);
}
