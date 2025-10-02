package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    CANCELLED("cancelled"),
    EXPIRED("expired");

    private final String value;

    ReservationStatus(String value) {
        this.value = value;
    }

    public static ReservationStatus fromValue(String value) {
        for (ReservationStatus status : ReservationStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
}
