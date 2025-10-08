package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.CheckType;
import com.webserver.evrentalsystem.entity.RentalCheck;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalCheckDto;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-04T20:16:03+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class RentalCheckMapperImpl implements RentalCheckMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RentalMapper rentalMapper;

    @Override
    public RentalCheckDto toRentalCheckDto(RentalCheck rentalCheck) {
        if ( rentalCheck == null ) {
            return null;
        }

        RentalCheckDto.RentalCheckDtoBuilder rentalCheckDto = RentalCheckDto.builder();

        rentalCheckDto.checkType( rentalCheckCheckTypeValue( rentalCheck ) );
        rentalCheckDto.id( rentalCheck.getId() );
        rentalCheckDto.rental( rentalMapper.toRentalDto( rentalCheck.getRental() ) );
        rentalCheckDto.staff( userMapper.toUserDto( rentalCheck.getStaff() ) );
        rentalCheckDto.conditionReport( rentalCheck.getConditionReport() );
        rentalCheckDto.photoUrl( rentalCheck.getPhotoUrl() );
        rentalCheckDto.customerSignatureUrl( rentalCheck.getCustomerSignatureUrl() );
        rentalCheckDto.staffSignatureUrl( rentalCheck.getStaffSignatureUrl() );
        rentalCheckDto.createdAt( rentalCheck.getCreatedAt() );

        return rentalCheckDto.build();
    }

    private String rentalCheckCheckTypeValue(RentalCheck rentalCheck) {
        if ( rentalCheck == null ) {
            return null;
        }
        CheckType checkType = rentalCheck.getCheckType();
        if ( checkType == null ) {
            return null;
        }
        String value = checkType.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }
}
