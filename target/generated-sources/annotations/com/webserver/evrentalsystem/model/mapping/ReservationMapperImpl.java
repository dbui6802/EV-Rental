package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Reservation;
import com.webserver.evrentalsystem.entity.ReservationStatus;
import com.webserver.evrentalsystem.entity.Vehicle;
import com.webserver.evrentalsystem.model.dto.entitydto.ReservationDto;
import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-15T10:58:28+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
)
@Component
public class ReservationMapperImpl implements ReservationMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StationMapper stationMapper;

    @Override
    public ReservationDto toReservationDto(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }

        ReservationDto reservationDto = new ReservationDto();

        reservationDto.setStatus( reservationStatusValue( reservation ) );
        reservationDto.setId( reservation.getId() );
        reservationDto.setRenter( userMapper.toUserDto( reservation.getRenter() ) );
        reservationDto.setVehicle( vehicleToVehicleDto( reservation.getVehicle() ) );
        reservationDto.setReservedStartTime( reservation.getReservedStartTime() );
        reservationDto.setReservedEndTime( reservation.getReservedEndTime() );
        reservationDto.setCancelledBy( userMapper.toUserDto( reservation.getCancelledBy() ) );
        reservationDto.setCancelledReason( reservation.getCancelledReason() );
        reservationDto.setCreatedAt( reservation.getCreatedAt() );

        return reservationDto;
    }

    private String reservationStatusValue(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }
        ReservationStatus status = reservation.getStatus();
        if ( status == null ) {
            return null;
        }
        String value = status.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }

    protected VehicleDto vehicleToVehicleDto(Vehicle vehicle) {
        if ( vehicle == null ) {
            return null;
        }

        VehicleDto.VehicleDtoBuilder vehicleDto = VehicleDto.builder();

        vehicleDto.id( vehicle.getId() );
        vehicleDto.licensePlate( vehicle.getLicensePlate() );
        if ( vehicle.getType() != null ) {
            vehicleDto.type( vehicle.getType().name() );
        }
        vehicleDto.brand( vehicle.getBrand() );
        vehicleDto.model( vehicle.getModel() );
        vehicleDto.capacity( vehicle.getCapacity() );
        vehicleDto.rangePerFullCharge( vehicle.getRangePerFullCharge() );
        if ( vehicle.getStatus() != null ) {
            vehicleDto.status( vehicle.getStatus().name() );
        }
        vehicleDto.pricePerHour( vehicle.getPricePerHour() );
        vehicleDto.station( stationMapper.toStationDto( vehicle.getStation() ) );

        return vehicleDto.build();
    }
}
