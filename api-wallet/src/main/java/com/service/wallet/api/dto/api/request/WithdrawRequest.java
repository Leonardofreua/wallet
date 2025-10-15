package com.service.wallet.api.dto.api.request;

import com.service.wallet.api.type.Amount;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WithdrawRequest(
    
    @Positive
    long userId,

    @NotNull
    @Valid
    Amount amount) {
}