package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum VehicleType {
    MOTORBIKE("motorbike"),
    CAR("car");

    private final String value;

    VehicleType(String value) {
        this.value = value;
    }

    public static VehicleType fromValue(String value) {
        for (VehicleType type : VehicleType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}
