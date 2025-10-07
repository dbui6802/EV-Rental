package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Rental;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, VehicleMapper.class, StationMapper.class})
public interface RentalMapper {
    @Mapping(source = "rentalType.value", target = "rentalType")
    @Mapping(source = "depositStatus.value", target = "depositStatus")
    @Mapping(source = "status.value", target = "status")
    RentalDto toRentalDto(Rental rental);
}
