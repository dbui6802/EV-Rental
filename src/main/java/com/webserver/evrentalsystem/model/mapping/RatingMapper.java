package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Rating;
import com.webserver.evrentalsystem.model.dto.entitydto.RatingDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, RentalMapper.class})
public interface RatingMapper {
    RatingDto toRatingDto(Rating rating);
}
