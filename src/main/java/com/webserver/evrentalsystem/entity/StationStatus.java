package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum StationStatus {
    ACTIVE("active"),
    INACTIVE("inactive"),
    MAINTENANCE("maintenance");

    private final String value;

    StationStatus(String value) {
        this.value = value;
    }

    public static StationStatus fromValue(String value) {
        for (StationStatus status : StationStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
}
