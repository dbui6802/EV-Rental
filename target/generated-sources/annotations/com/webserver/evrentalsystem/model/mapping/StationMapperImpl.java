package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Station;
import com.webserver.evrentalsystem.entity.StationStatus;
import com.webserver.evrentalsystem.model.dto.entitydto.StationDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-04T20:16:03+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class StationMapperImpl implements StationMapper {

    @Override
    public StationDto toStationDto(Station station) {
        if ( station == null ) {
            return null;
        }

        StationDto stationDto = new StationDto();

        stationDto.setStatus( stationStatusValue( station ) );
        stationDto.setId( station.getId() );
        stationDto.setName( station.getName() );
        stationDto.setAddress( station.getAddress() );
        stationDto.setLatitude( station.getLatitude() );
        stationDto.setLongitude( station.getLongitude() );

        return stationDto;
    }

    private String stationStatusValue(Station station) {
        if ( station == null ) {
            return null;
        }
        StationStatus status = station.getStatus();
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
