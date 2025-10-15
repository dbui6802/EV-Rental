package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Violation;
import com.webserver.evrentalsystem.model.dto.entitydto.ViolationDto;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-15T08:53:04+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
)
@Component
public class ViolationMapperImpl implements ViolationMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RentalMapper rentalMapper;

    @Override
    public ViolationDto toViolationDto(Violation violation) {
        if ( violation == null ) {
            return null;
        }

        ViolationDto.ViolationDtoBuilder violationDto = ViolationDto.builder();

        violationDto.id( violation.getId() );
        violationDto.rental( rentalMapper.toRentalDto( violation.getRental() ) );
        violationDto.staff( userMapper.toUserDto( violation.getStaff() ) );
        violationDto.description( violation.getDescription() );
        violationDto.fineAmount( violation.getFineAmount() );
        violationDto.createdAt( violation.getCreatedAt() );

        return violationDto.build();
    }
}
