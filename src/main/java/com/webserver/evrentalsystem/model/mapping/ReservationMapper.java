package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Reservation;
import com.webserver.evrentalsystem.model.dto.entitydto.ReservationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, StationMapper.class})
public interface ReservationMapper {
    @Mapping(source = "status.value", target = "status") // enum ReservationStatus -> string
    ReservationDto toReservationDto(Reservation reservation);
}
