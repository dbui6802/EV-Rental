package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum IncidentStatus {
    PENDING("pending"),
    IN_REVIEW("in_review"),
    RESOLVED("resolved");

    private final String value;

    IncidentStatus(String value) {
        this.value = value;
    }

    public static IncidentStatus fromValue(String value) {
        for (IncidentStatus s : IncidentStatus.values()) {
            if (s.value.equals(value)) {
                return s;
            }
        }
        return null;
    }
}
