package com.webserver.evrentalsystem.exception;

import com.webserver.evrentalsystem.exception.store.ErrorSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    @Autowired
    private ErrorSaver errorSaver;

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundException(UserNotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), Error.UserNotFound.getValue(), ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundException(NotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), Error.NotFound.getValue(), ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse handleConflictException(ConflictException ex) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), Error.Conflict.getValue(), ex.getMessage());
    }

    @ExceptionHandler(InvalidateParamsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInvalidateParamsException(InvalidateParamsException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), Error.InvalidateParamsException.getValue(), ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), Error.UserAlreadyExists.getValue(), ex.getMessage());
    }

    @ExceptionHandler(ExpiredAccessTokenException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse handleInvalidTokenException(ExpiredAccessTokenException ex) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), Error.ExpiredAccessToken.getValue(), ex.getMessage());
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse handleExpiredRefreshTokenException(ExpiredRefreshTokenException ex) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), Error.ExpiredRefreshToken.getValue(), ex.getMessage());
    }

    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handlePermissionDeniedException(PermissionDeniedException ex) {
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), Error.PermissionDenied.getValue(), ex.getMessage());
    }

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleInternalServerException(InternalServerException ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), Error.InternalServer.getValue(), ex.getMessage());
    }

    // Catch validation errors like @NotNull, @Size, ...
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Extract validation error messages
        String messages = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                .orElse("Validation failed");

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                messages
        );

        return ResponseEntity.badRequest().body(response);
    }

    // for remaining exceptions
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleException(Exception ex) {
        // save the exception to the database for further investigation
        errorSaver.saveErrorToDatabase(ex);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), Error.InternalServer.getValue(), ex.getMessage());
    }
}
