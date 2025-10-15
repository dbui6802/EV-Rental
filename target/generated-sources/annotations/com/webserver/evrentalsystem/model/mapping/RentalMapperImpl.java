package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.DepositStatus;
import com.webserver.evrentalsystem.entity.Rental;
import com.webserver.evrentalsystem.entity.RentalStatus;
import com.webserver.evrentalsystem.entity.RentalType;
import com.webserver.evrentalsystem.model.dto.entitydto.RentalDto;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-15T08:53:04+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
)
@Component
public class RentalMapperImpl implements RentalMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VehicleMapper vehicleMapper;
    @Autowired
    private StationMapper stationMapper;

    @Override
    public RentalDto toRentalDto(Rental rental) {
        if ( rental == null ) {
            return null;
        }

        RentalDto.RentalDtoBuilder rentalDto = RentalDto.builder();

        rentalDto.rentalType( rentalRentalTypeValue( rental ) );
        rentalDto.depositStatus( rentalDepositStatusValue( rental ) );
        rentalDto.status( rentalStatusValue( rental ) );
        rentalDto.id( rental.getId() );
        rentalDto.renter( userMapper.toUserDto( rental.getRenter() ) );
        rentalDto.vehicle( vehicleMapper.toVehicleDto( rental.getVehicle() ) );
        rentalDto.stationPickup( stationMapper.toStationDto( rental.getStationPickup() ) );
        rentalDto.stationReturn( stationMapper.toStationDto( rental.getStationReturn() ) );
        rentalDto.staffPickup( userMapper.toUserDto( rental.getStaffPickup() ) );
        rentalDto.staffReturn( userMapper.toUserDto( rental.getStaffReturn() ) );
        rentalDto.startTime( rental.getStartTime() );
        rentalDto.endTime( rental.getEndTime() );
        rentalDto.totalDistance( rental.getTotalDistance() );
        rentalDto.totalCost( rental.getTotalCost() );
        rentalDto.depositAmount( rental.getDepositAmount() );
        rentalDto.createdAt( rental.getCreatedAt() );

        return rentalDto.build();
    }

    private String rentalRentalTypeValue(Rental rental) {
        if ( rental == null ) {
            return null;
        }
        RentalType rentalType = rental.getRentalType();
        if ( rentalType == null ) {
            return null;
        }
        String value = rentalType.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }

    private String rentalDepositStatusValue(Rental rental) {
        if ( rental == null ) {
            return null;
        }
        DepositStatus depositStatus = rental.getDepositStatus();
        if ( depositStatus == null ) {
            return null;
        }
        String value = depositStatus.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }

    private String rentalStatusValue(Rental rental) {
        if ( rental == null ) {
            return null;
        }
        RentalStatus status = rental.getStatus();
        if ( status == null ) {
            return null;
        }
        String value = status.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }
}
