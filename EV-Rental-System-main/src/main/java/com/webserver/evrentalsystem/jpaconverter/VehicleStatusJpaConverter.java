package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.VehicleStatus;
import jakarta.persistence.AttributeConverter;

public class VehicleStatusJpaConverter implements AttributeConverter<VehicleStatus, String> {

    @Override
    public String convertToDatabaseColumn(VehicleStatus status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public VehicleStatus convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return VehicleStatus.fromValue(value);
    }
}
