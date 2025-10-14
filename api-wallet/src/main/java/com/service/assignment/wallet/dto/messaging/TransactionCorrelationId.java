package com.service.assignment.wallet.dto.messaging;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record TransactionCorrelationId(@NotNull UUID transactionCorrelationId) {
    
}
