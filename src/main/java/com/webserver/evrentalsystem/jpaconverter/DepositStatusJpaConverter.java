package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.DepositStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DepositStatusJpaConverter implements AttributeConverter<DepositStatus, String> {
    @Override
    public String convertToDatabaseColumn(DepositStatus status) {
        return status == null ? null : status.getValue();
    }

    @Override
    public DepositStatus convertToEntityAttribute(String value) {
        return value == null ? null : DepositStatus.fromValue(value);
    }
}
