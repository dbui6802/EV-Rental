package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum Role {
    RENTER("renter"),
    STAFF("staff"),
    ADMIN("admin");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public static Role fromValue(String value) {
        for (Role role : Role.values()) {
            if (role.value.equals(value)) {
                return role;
            }
        }
        return null;
    }
}
