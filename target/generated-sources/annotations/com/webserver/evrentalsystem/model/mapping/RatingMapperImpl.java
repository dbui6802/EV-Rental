package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Rating;
import com.webserver.evrentalsystem.model.dto.entitydto.RatingDto;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-15T08:53:04+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
)
@Component
public class RatingMapperImpl implements RatingMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RentalMapper rentalMapper;

    @Override
    public RatingDto toRatingDto(Rating rating) {
        if ( rating == null ) {
            return null;
        }

        RatingDto.RatingDtoBuilder ratingDto = RatingDto.builder();

        ratingDto.id( rating.getId() );
        ratingDto.rental( rentalMapper.toRentalDto( rating.getRental() ) );
        ratingDto.renter( userMapper.toUserDto( rating.getRenter() ) );
        ratingDto.rating( rating.getRating() );
        ratingDto.comment( rating.getComment() );
        ratingDto.createdAt( rating.getCreatedAt() );

        return ratingDto.build();
    }
}
