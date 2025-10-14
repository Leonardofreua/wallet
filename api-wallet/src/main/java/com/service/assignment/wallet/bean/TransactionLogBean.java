package com.service.assignment.wallet.bean;

import com.service.assignment.wallet.domain.Wallet;
import com.service.assignment.wallet.type.Amount;
import com.service.assignment.wallet.enums.OperationType;

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
