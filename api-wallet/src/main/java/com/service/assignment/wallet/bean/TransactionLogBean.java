package com.service.assignment.wallet.bean;

import com.service.assignment.wallet.domain.Wallet;
import com.service.assignment.wallet.type.Amount;
import com.service.assignment.wallet.enums.OperationType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record TransactionLogBean(

        @Valid
        Wallet sourceWallet,

        @Valid 
        Wallet targetWallet,

        @Valid
        @NotNull
        Amount amount,

        @NotNull
        OperationType operationType) {

    public static TransactionLogBean buildDeposit(Wallet targetWallet, Amount amount) {
        return new TransactionLogBean(null, targetWallet, amount, OperationType.DEPOSIT);
    }

    public static TransactionLogBean buildTransfer(Wallet sourceWallet, Wallet targetWallet, Amount amount) {
        return new TransactionLogBean(sourceWallet, targetWallet, amount, OperationType.TRANSFER);
    }

    public static TransactionLogBean buildWithdraw(Wallet sourceWallet, Amount amount) {
        return new TransactionLogBean(sourceWallet, null, amount, OperationType.WITHDRAW);
    }
}
