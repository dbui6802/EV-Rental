package com.webserver.evrentalsystem.exception;

public class ExpiredAccessTokenException extends RuntimeException {
    public ExpiredAccessTokenException(String message) {
        super(message);
    }
}
