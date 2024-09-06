package com.liemartt.cloud.exception;

public class NotEnoughSpaceException extends RuntimeException {
    public NotEnoughSpaceException(String message) {
        super(message);
    }
}
