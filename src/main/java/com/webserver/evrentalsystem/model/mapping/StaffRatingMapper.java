package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.StaffRating;
import com.webserver.evrentalsystem.model.dto.entitydto.StaffRatingDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, RentalMapper.class})
public interface StaffRatingMapper {
    StaffRatingDto toStaffRatingDto(StaffRating staffRating);
}
