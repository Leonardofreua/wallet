package com.service.wallet.api.bean;

import com.service.wallet.api.type.Amount;

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
