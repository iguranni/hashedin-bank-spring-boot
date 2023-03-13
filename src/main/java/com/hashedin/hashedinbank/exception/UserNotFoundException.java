package com.hashedin.hashedinbank.exception;

public class UserNotFoundException extends IllegalArgumentException {
    public UserNotFoundException(String message) {
        super(message);

    }
}
