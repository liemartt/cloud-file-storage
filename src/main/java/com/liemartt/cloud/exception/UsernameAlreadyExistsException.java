package com.liemartt.cloud.exception;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
