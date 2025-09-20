package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.RentalType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RentalTypeJpaConverter implements AttributeConverter<RentalType, String> {
    @Override
    public String convertToDatabaseColumn(RentalType type) {
        return type == null ? null : type.getValue();
    }

    @Override
    public RentalType convertToEntityAttribute(String value) {
        return value == null ? null : RentalType.fromValue(value);
    }
}
