package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum ComplaintStatus {
    PENDING("pending"),
    IN_REVIEW("in_review"),
    RESOLVED("resolved"),
    REJECTED("rejected");

    private final String value;

    ComplaintStatus(String value) {
        this.value = value;
    }

    public static ComplaintStatus fromValue(String value) {
        for (ComplaintStatus status : ComplaintStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}
