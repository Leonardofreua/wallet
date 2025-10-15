package com.service.wallet.api.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(long userId) {
        super("User not found for ID: " + userId);
    }
}
