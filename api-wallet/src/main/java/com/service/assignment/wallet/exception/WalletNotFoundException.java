package com.service.assignment.wallet.exception;

import lombok.experimental.StandardException;

@StandardException
public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException(long walletId) {
        super("Wallet not found for ID: " + walletId);
    }
}
