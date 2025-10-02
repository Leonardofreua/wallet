package com.service.assignment.wallet.service;

import static com.service.assignment.wallet.enums.OperationType.DEPOSIT;
import static com.service.assignment.wallet.enums.OperationType.TRANSFER;
import static com.service.assignment.wallet.enums.OperationType.WITHDRAW;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for calculating the historical balance of a wallet at a
 * specific timestamp.
 * 
 * <pre>
 * This service uses the {@link TransactionLogService} to query all relevant transactions
 * that occurred before the provided date and time, including deposits, withdrawals, and
 * transfers (both sent and received).
 * 
 * The historical balance is computed as:
 * 
 * (sum of deposits + sum of received transfers) - (sum of withdrawals + sum of sent transfers)
 * </pre>
 * 
 * @author Leonardo Freua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HistoricalBalanceService {

    private final TransactionLogService transactionLogService;

    /**
     * Calculates the historical balance of a wallet as of a specific date and time.
     *
     * @param sourceWalletId the ID of the wallet whose balance is being queried
     * @param createdAt      the point in time for which the balance should be
     *                       calculated
     * @return the computed historical balance as a {@link BigDecimal}
     * @throws IllegalArgumentException if the wallet ID is not positive or if the
     *                                  timestamp is null
     */
    public BigDecimal getHistoricalBalance(@Positive long sourceWalletId, @NotNull LocalDateTime createdAt) {
        log.info("Request historical balance for wallet {} at date and time {}", sourceWalletId, createdAt);
        BigDecimal deposits = transactionLogService.sumAmountByOperationBefore(sourceWalletId, DEPOSIT, createdAt);
        BigDecimal withdrawals = transactionLogService.sumAmountByOperationBefore(sourceWalletId, WITHDRAW, createdAt);
        BigDecimal transfersSent = transactionLogService.sumAmountByOperationBefore(sourceWalletId, TRANSFER,
                createdAt);
        BigDecimal transfersReceived = transactionLogService.sumReceivedTransfersBefore(sourceWalletId, createdAt);

        return deposits
                .add(transfersReceived)
                .subtract(withdrawals)
                .subtract(transfersSent);
    }
}
