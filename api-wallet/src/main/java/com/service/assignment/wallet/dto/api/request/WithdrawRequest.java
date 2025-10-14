package com.service.assignment.wallet.dto.api.request;

import com.service.assignment.wallet.type.Amount;

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