package com.service.assignment.wallet.bean;

import com.service.assignment.wallet.domain.type.Amount;

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
