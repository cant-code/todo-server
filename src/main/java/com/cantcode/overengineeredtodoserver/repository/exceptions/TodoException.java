package com.cantcode.overengineeredtodoserver.repository.exceptions;

import org.springframework.http.HttpStatus;

public class TodoException extends RuntimeException {

    private final HttpStatus httpStatus;

    public TodoException(HttpStatus httpStatus, String reason) {
        super(reason);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
