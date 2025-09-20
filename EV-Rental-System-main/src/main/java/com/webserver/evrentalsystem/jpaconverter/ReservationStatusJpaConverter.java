package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.ReservationStatus;
import jakarta.persistence.AttributeConverter;

public class ReservationStatusJpaConverter implements AttributeConverter<ReservationStatus, String> {
    @Override
    public String convertToDatabaseColumn(ReservationStatus status) {
        return status == null ? null : status.getValue();
    }

    @Override
    public ReservationStatus convertToEntityAttribute(String value) {
        return value == null ? null : ReservationStatus.fromValue(value);
    }
}
