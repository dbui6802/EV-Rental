package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum IncidentSeverity {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high");

    private final String value;

    IncidentSeverity(String value) {
        this.value = value;
    }

    public static IncidentSeverity fromValue(String value) {
        for (IncidentSeverity s : IncidentSeverity.values()) {
            if (s.value.equals(value)) {
                return s;
            }
        }
        return null;
    }
}
