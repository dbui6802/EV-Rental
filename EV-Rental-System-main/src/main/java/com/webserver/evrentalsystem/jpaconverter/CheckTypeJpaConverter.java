package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.CheckType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CheckTypeJpaConverter implements AttributeConverter<CheckType, String> {
    @Override
    public String convertToDatabaseColumn(CheckType type) {
        return type == null ? null : type.getValue();
    }

    @Override
    public CheckType convertToEntityAttribute(String value) {
        return value == null ? null : CheckType.fromValue(value);
    }
}
