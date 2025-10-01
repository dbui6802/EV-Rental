package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.ComplaintStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ComplaintStatusConverter implements AttributeConverter<ComplaintStatus, String> {

    @Override
    public String convertToDatabaseColumn(ComplaintStatus status) {
        return status == null ? null : status.getValue();
    }

    @Override
    public ComplaintStatus convertToEntityAttribute(String value) {
        return value == null ? null : ComplaintStatus.fromValue(value);
    }
}
