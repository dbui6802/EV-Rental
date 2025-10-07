package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Vehicle;
import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StationMapper.class})
public interface VehicleMapper {
    @Mapping(source = "type.value", target = "type")       // enum VehicleType -> string
    @Mapping(source = "status.value", target = "status")   // enum VehicleStatus -> string
    VehicleDto toVehicleDto(Vehicle vehicle);
}
