package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum RentalStatus {
    BOOKED("booked"),
    IN_USE("in_use"),
    RETURNED("returned"),
    CANCELLED("cancelled");

    private final String value;

    RentalStatus(String value) {
        this.value = value;
    }

    public static RentalStatus fromValue(String value) {
        for (RentalStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
}
