package com.webserver.evrentalsystem.exception;

import lombok.Getter;

@Getter
public enum Error {
    UserNotFound("UserNotFound"),
    NotFound("NotFound"),
    Conflict("Conflict"),
    InvalidateParamsException("InvalidateParamsException"),
    UserAlreadyExists("UserAlreadyExists"),
    ExpiredAccessToken("ExpiredAccessToken"),
    ExpiredRefreshToken("ExpiredRefreshToken"),
    PermissionDenied("PermissionDenied"),
    UserIsBlocked("UserIsBlocked"),
    InternalServer("InternalServer");

    private final String value;

    Error(String value) {
        this.value = value;
    }
}
