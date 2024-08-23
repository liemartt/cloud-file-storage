package com.liemartt.cloud.exception;

public class InvalidUserRequestException extends RuntimeException{
    public InvalidUserRequestException(String message) {
        super(message);
    }
}
