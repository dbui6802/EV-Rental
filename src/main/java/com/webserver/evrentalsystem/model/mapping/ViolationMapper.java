package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Violation;
import com.webserver.evrentalsystem.model.dto.entitydto.ViolationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, RentalMapper.class})
public interface ViolationMapper {
    ViolationDto toViolationDto(Violation violation);
}
