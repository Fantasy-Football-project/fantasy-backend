package com.sathvik.exceptions;

import org.springframework.http.HttpStatus;

//This is a custom exception.
public class AppException extends RuntimeException {

    private final HttpStatus code;

    public AppException(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }

    public HttpStatus getCode() {
        return code;
    }
}
