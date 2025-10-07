package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.StationStatus;
import jakarta.persistence.AttributeConverter;

public class StationStatusJpaConverter implements AttributeConverter<StationStatus, String> {
    @Override
    public String convertToDatabaseColumn(StationStatus status) {
        return status == null ? null : status.getValue();
    }

    @Override
    public StationStatus convertToEntityAttribute(String value) {
        return value == null ? null : StationStatus.fromValue(value);
    }
}
