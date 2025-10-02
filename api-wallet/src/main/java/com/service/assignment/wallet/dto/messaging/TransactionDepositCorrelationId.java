package com.service.assignment.wallet.dto.messaging;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record TransactionDepositCorrelationId(@NotNull UUID transactionCorrelationId) {
    
}
