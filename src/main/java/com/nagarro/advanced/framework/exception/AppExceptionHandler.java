package com.nagarro.advanced.framework.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {AppException.class})
    public ResponseEntity<Object> handleCustomException(AppException appException) {
        ErrorResponse apiException = new ErrorResponse(
                LocalDateTime.now(),
                appException.getHttpStatus(),
                appException.getMessage());
        return new ResponseEntity<>(apiException, appException.getHttpStatus());
    }
}