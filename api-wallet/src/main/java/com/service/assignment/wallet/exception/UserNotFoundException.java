package com.service.assignment.wallet.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(long userId) {
        super("User not found for ID: " + userId);
    }
}
