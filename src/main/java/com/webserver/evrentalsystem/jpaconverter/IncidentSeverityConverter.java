package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.IncidentSeverity;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class IncidentSeverityConverter implements AttributeConverter<IncidentSeverity, String> {

    @Override
    public String convertToDatabaseColumn(IncidentSeverity severity) {
        return severity == null ? null : severity.getValue();
    }

    @Override
    public IncidentSeverity convertToEntityAttribute(String value) {
        return value == null ? null : IncidentSeverity.fromValue(value);
    }
}
