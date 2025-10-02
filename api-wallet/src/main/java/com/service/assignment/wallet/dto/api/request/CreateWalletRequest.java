package com.service.assignment.wallet.dto.api.request;

import jakarta.validation.constraints.Positive;

public record CreateWalletRequest(@Positive long userId) {

}
