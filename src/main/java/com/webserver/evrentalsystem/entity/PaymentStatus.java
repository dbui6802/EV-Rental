package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING("pending"),
    PAID("paid"),
    FAILED("failed");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public static PaymentStatus fromValue(String value) {
        for (PaymentStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
}
