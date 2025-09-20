package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum VehicleStatus {
    RESERVED("reserved"),
    AVAILABLE("available"),
    RENTED("rented"),
    MAINTENANCE("maintenance");

    private final String value;

    VehicleStatus(String value) {
        this.value = value;
    }

    public static VehicleStatus fromValue(String value) {
        for (VehicleStatus status : VehicleStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
}
