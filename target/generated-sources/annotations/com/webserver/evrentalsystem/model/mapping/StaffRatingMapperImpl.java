package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.StaffRating;
import com.webserver.evrentalsystem.model.dto.entitydto.StaffRatingDto;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-15T10:58:28+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
)
@Component
public class StaffRatingMapperImpl implements StaffRatingMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RentalMapper rentalMapper;

    @Override
    public StaffRatingDto toStaffRatingDto(StaffRating staffRating) {
        if ( staffRating == null ) {
            return null;
        }

        StaffRatingDto.StaffRatingDtoBuilder staffRatingDto = StaffRatingDto.builder();

        staffRatingDto.id( staffRating.getId() );
        staffRatingDto.rental( rentalMapper.toRentalDto( staffRating.getRental() ) );
        staffRatingDto.renter( userMapper.toUserDto( staffRating.getRenter() ) );
        staffRatingDto.staff( userMapper.toUserDto( staffRating.getStaff() ) );
        staffRatingDto.rating( staffRating.getRating() );
        staffRatingDto.comment( staffRating.getComment() );
        staffRatingDto.createdAt( staffRating.getCreatedAt() );

        return staffRatingDto.build();
    }
}
