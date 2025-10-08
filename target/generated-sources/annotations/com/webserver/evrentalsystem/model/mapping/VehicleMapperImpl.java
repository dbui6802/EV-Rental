package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Vehicle;
import com.webserver.evrentalsystem.entity.VehicleStatus;
import com.webserver.evrentalsystem.entity.VehicleType;
import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-04T20:16:03+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class VehicleMapperImpl implements VehicleMapper {

    @Autowired
    private StationMapper stationMapper;

    @Override
    public VehicleDto toVehicleDto(Vehicle vehicle) {
        if ( vehicle == null ) {
            return null;
        }

        VehicleDto.VehicleDtoBuilder vehicleDto = VehicleDto.builder();

        vehicleDto.type( vehicleTypeValue( vehicle ) );
        vehicleDto.status( vehicleStatusValue( vehicle ) );
        vehicleDto.id( vehicle.getId() );
        vehicleDto.licensePlate( vehicle.getLicensePlate() );
        vehicleDto.brand( vehicle.getBrand() );
        vehicleDto.model( vehicle.getModel() );
        vehicleDto.capacity( vehicle.getCapacity() );
        vehicleDto.rangePerFullCharge( vehicle.getRangePerFullCharge() );
        vehicleDto.pricePerHour( vehicle.getPricePerHour() );
        vehicleDto.station( stationMapper.toStationDto( vehicle.getStation() ) );

        return vehicleDto.build();
    }

    private String vehicleTypeValue(Vehicle vehicle) {
        if ( vehicle == null ) {
            return null;
        }
        VehicleType type = vehicle.getType();
        if ( type == null ) {
            return null;
        }
        String value = type.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }

    private String vehicleStatusValue(Vehicle vehicle) {
        if ( vehicle == null ) {
            return null;
        }
        VehicleStatus status = vehicle.getStatus();
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
