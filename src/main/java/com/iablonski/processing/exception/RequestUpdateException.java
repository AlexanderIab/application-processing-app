package com.iablonski.processing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)

public class RequestUpdateException extends RuntimeException {
    public RequestUpdateException(String message) {
        super(message);
    }
}