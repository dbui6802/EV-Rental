package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum CheckType {
    PICKUP("pickup"),
    RETURN("return");

    private final String value;

    CheckType(String value) {
        this.value = value;
    }

    public static CheckType fromValue(String value) {
        for (CheckType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}
