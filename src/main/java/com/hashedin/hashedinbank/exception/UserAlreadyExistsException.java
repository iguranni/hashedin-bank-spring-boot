package com.hashedin.hashedinbank.exception;

public class UserAlreadyExistsException extends IllegalArgumentException {
    public UserAlreadyExistsException(String message) {
        super(message);

    }
}
