package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum RentalType {
    BOOKING("booking"),
    WALK_IN("walk-in");

    private final String value;

    RentalType(String value) {
        this.value = value;
    }

    public static RentalType fromValue(String value) {
        for (RentalType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}
