package com.service.assignment.wallet.bean;

import com.service.assignment.wallet.type.Amount;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WithdrawBean(

    @Positive
    long walletId,

    @Positive
    long userId,

    @NotNull
    @Valid
    Amount amount) {

}
