package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum DepositStatus {
    PENDING("pending"),
    HELD("held"),
    REFUNDED("refunded");

    private final String value;

    DepositStatus(String value) {
        this.value = value;
    }

    public static DepositStatus fromValue(String value) {
        for (DepositStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
}
