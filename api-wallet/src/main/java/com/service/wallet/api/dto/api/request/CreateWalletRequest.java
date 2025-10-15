package com.service.wallet.api.dto.api.request;

import jakarta.validation.constraints.Positive;

public record CreateWalletRequest(@Positive long userId) {

}
