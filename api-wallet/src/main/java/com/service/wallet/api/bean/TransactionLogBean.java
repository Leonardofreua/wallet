package com.service.wallet.api.bean;

import com.service.wallet.api.domain.Wallet;
import com.service.wallet.api.type.Amount;
import com.service.wallet.api.enums.OperationType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TransactionLogBean(

        @Valid
        Wallet sourceWallet,

        @Valid 
        Wallet targetWallet,

        @Valid
        @NotNull
        Amount amount,

        @NotNull
        UUID correlationId,

        @NotNull
        OperationType operationType) {

    public static TransactionLogBean buildDeposit(Wallet targetWallet, Amount amount, UUID correlationId) {
        return new TransactionLogBean(null, targetWallet, amount, correlationId, OperationType.DEPOSIT);
    }

    public static TransactionLogBean buildTransfer(Wallet sourceWallet, Wallet targetWallet, Amount amount, UUID correlationId) {
        return new TransactionLogBean(sourceWallet, targetWallet, amount, correlationId, OperationType.TRANSFER);
    }

    public static TransactionLogBean buildWithdraw(Wallet sourceWallet, Amount amount, UUID correlationId) {
        return new TransactionLogBean(sourceWallet, null, amount, correlationId, OperationType.WITHDRAW);
    }
}
