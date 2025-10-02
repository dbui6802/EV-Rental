package com.webserver.evrentalsystem.entity;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CASH("cash"),
    CARD("card"),
    E_WALLET("e-wallet");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public static PaymentMethod fromValue(String value) {
        for (PaymentMethod method : values()) {
            if (method.value.equalsIgnoreCase(value)) {
                return method;
            }
        }
        return null;
    }
}
