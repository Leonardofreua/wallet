package com.service.wallet.processor.dto.messaging;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TransactionCorrelationId(@NotNull UUID transactionCorrelationId) {
    
}
