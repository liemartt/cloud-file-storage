package com.liemartt.cloud.exception;

public class InvalidUserSignUpRequestException extends RuntimeException{
    public InvalidUserSignUpRequestException(String message) {
        super(message);
    }
}
