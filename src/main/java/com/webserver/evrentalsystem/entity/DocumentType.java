package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum DocumentType {
    CMND("CMND"),
    CCCD("CCCD"),
    GPLX("GPLX");

    private final String value;

    DocumentType(String value) {
        this.value = value;
    }

    public static DocumentType fromValue(String value) {
        for (DocumentType type : DocumentType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}