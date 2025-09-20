package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum ErrorStatus {
    CLOSED("closed"),
    OPENED("opened"),
    REOPENED("reopened");

    private final String value;

    ErrorStatus(String value) {
        this.value = value;
    }

    public static ErrorStatus fromValue(String value) {
        for (ErrorStatus errorStatus : ErrorStatus.values()) {
            if (errorStatus.value.equals(value)) {
                return errorStatus;
            }
        }
        return null;
    }
}
