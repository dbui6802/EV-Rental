package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.IncidentStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class IncidentStatusConverter implements AttributeConverter<IncidentStatus, String> {

    @Override
    public String convertToDatabaseColumn(IncidentStatus status) {
        return status == null ? null : status.getValue();
    }

    @Override
    public IncidentStatus convertToEntityAttribute(String value) {
        return value == null ? null : IncidentStatus.fromValue(value);
    }
}
