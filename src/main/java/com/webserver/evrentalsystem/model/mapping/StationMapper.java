package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Station;
import com.webserver.evrentalsystem.model.dto.entitydto.StationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StationMapper {
    @Mapping(source = "status.value", target = "status")
    StationDto toStationDto(Station station);
}
