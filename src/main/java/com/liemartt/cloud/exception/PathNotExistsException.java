package com.liemartt.cloud.exception;

public class PathNotExistsException extends RuntimeException {
    public PathNotExistsException() {
        super("No such path");
    }
}
