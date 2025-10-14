package com.service.assignment.wallet.bean;

import com.service.assignment.wallet.type.Amount;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferBean(
    
    @Positive
    long sourceWalletId,
    
    @Positive
    long sourceUserId,
    
    @Positive
    long targetUserId,
    
    @NotNull
    @Valid
    Amount amount) {
    
}
