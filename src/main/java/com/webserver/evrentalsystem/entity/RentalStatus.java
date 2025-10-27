package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum RentalStatus {
    BOOKED("booked"),
    WAIT_CONFIRM("wait_confirm"),
    IN_USE("in_use"),
    WAITING_FOR_PAYMENT("waiting_for_payment"),
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
