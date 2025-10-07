package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.RentalCheck;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalCheckDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, RentalMapper.class})
public interface RentalCheckMapper {
    @Mapping(source = "checkType.value", target = "checkType")
    RentalCheckDto toRentalCheckDto(RentalCheck rentalCheck);
}
