package com.doin.signal.exception;

public class BadResourceRequestException extends RuntimeException {
    public BadResourceRequestException(String message) {
        super(message);
    }
}
