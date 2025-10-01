package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.VehicleType;
import jakarta.persistence.AttributeConverter;

public class VehicleTypeJpaConverter implements AttributeConverter<VehicleType, String> {

    @Override
    public String convertToDatabaseColumn(VehicleType type) {
        if (type == null) {
            return null;
        }
        return type.getValue();
    }

    @Override
    public VehicleType convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return VehicleType.fromValue(value);
    }
}
