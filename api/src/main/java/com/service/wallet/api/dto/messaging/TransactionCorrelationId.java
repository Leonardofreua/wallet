package com.service.wallet.api.dto.messaging;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record TransactionCorrelationId(@NotNull UUID transactionCorrelationId) {
    
}
