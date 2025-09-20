package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.ErrorStatus;
import jakarta.persistence.AttributeConverter;

public class ErrorStatusJpaConverter implements AttributeConverter<ErrorStatus, String> {
    @Override
    public String convertToDatabaseColumn(ErrorStatus errorStatus) {
        if (errorStatus == null) {
            return null;
        }
        return errorStatus.getValue();
    }

    @Override
    public ErrorStatus convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return ErrorStatus.fromValue(s);
    }
}
