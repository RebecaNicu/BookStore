package com.nagarro.advanced.framework.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime localDateTime, HttpStatus httpStatus, String message) {
}
