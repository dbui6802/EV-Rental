package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.RentalStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RentalStatusJpaConverter implements AttributeConverter<RentalStatus, String> {
    @Override
    public String convertToDatabaseColumn(RentalStatus status) {
        return status == null ? null : status.getValue();
    }

    @Override
    public RentalStatus convertToEntityAttribute(String value) {
        return value == null ? null : RentalStatus.fromValue(value);
    }
}
