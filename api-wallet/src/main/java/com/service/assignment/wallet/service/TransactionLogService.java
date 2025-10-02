package com.service.assignment.wallet.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.service.assignment.wallet.bean.TransactionLogBean;
import com.service.assignment.wallet.domain.TransactionLog;
import com.service.assignment.wallet.domain.TransactionLog.TransactionLogBuilder;
import com.service.assignment.wallet.enums.OperationType;
import com.service.assignment.wallet.enums.TransactionStatus;
import com.service.assignment.wallet.exception.TransactionLogException;
import com.service.assignment.wallet.repository.TransactionLogRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for logging and aggregating transaction data.
 * <p>
 * This service handles the creation of transaction log entries and provides
 * methods to calculate aggregated values such as total deposits, withdrawals,
 * and transfers before a specified point in time.
 * </p>
 * 
 * @author Leonardo Freua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionLogService {

    private final TransactionLogRepository repository;

    /**
     * Persists a new transaction log entry based on the provided bean.
     * <p>
     * Validates that a target wallet is provided when the transaction type is a
     * transfer.
     * </p>
     *
     * @param transactionLogBean the bean containing transaction details
     * @throws TransactionLogException if a transfer operation is missing the target
     *                                 wallet
     */
    @Transactional
    public void log(@NotNull @Valid TransactionLogBean transactionLogBean) {
        log.info("Transaction record requested with the following criteria: {}", transactionLogBean);
        var transaction = TransactionLog.builder()
                .sourceWallet(transactionLogBean.sourceWallet())
                .operation(transactionLogBean.operationType())
                .amount(transactionLogBean.amount().value())
                .createdAt(LocalDateTime.now());

        if (transactionLogBean.operationType() == OperationType.TRANSFER) {
            if (transactionLogBean.targetWallet() == null) {
                throw new TransactionLogException(
                        "It is mandatory to inform the target wallet to register the transfer type transaction.");
            }

            transaction.targetWallet(transactionLogBean.targetWallet());
        }

        repository.save(transaction.build());
    }

    @Transactional
    public TransactionLog logProcessing(@NotNull @Valid TransactionLogBean transactionLogBean) {
        log.info("Creating trasanction log with status processing and the following criteria: {}", transactionLogBean);
        var transactionLog = initializeTransaction(transactionLogBean, TransactionStatus.PROCESSING).build();

        return repository.save(transactionLog);
    }

    private static TransactionLogBuilder initializeTransaction(TransactionLogBean transactionLogBean,
            TransactionStatus status) {
        var transactionLogBuilder = TransactionLog.builder()
                .correlationId(UUID.randomUUID())
                .operation(transactionLogBean.operationType())
                .amount(transactionLogBean.amount().value())
                .currentStatus(status)
                .createdAt(LocalDateTime.now());

        if (transactionLogBean.operationType() != OperationType.DEPOSIT) {
            transactionLogBuilder.sourceWallet(transactionLogBean.sourceWallet());
        }

        if (transactionLogBean.operationType() != OperationType.WITHDRAW) {
            transactionLogBuilder.targetWallet(transactionLogBean.targetWallet());
        }

        if (transactionLogBean.operationType() == OperationType.TRANSFER) {
            if (transactionLogBean.targetWallet() == null) {
                throw new TransactionLogException(
                        "It is mandatory to inform the target wallet to register the transfer type transaction.");
            }

            transactionLogBuilder.targetWallet(transactionLogBean.targetWallet());
        }

        return transactionLogBuilder;
    }

    /**
     * Returns the total amount of a given operation type for a wallet,
     * limited to transactions that occurred before the specified date and time.
     *
     * @param sourceWalletId the ID of the wallet performing the operation
     * @param operation      the operation type (e.g., DEPOSIT, WITHDRAW)
     * @param createdAt      the timestamp threshold; only transactions before this
     *                       will be summed
     * @return the total amount or {@code BigDecimal.ZERO} if no transactions are
     *         found
     */
    public BigDecimal sumAmountByOperationBefore(@Positive long sourceWalletId, @NotNull OperationType operation,
            @NotNull LocalDateTime createdAt) {
        return repository.sumBySourceWalletIdAndTypeBefore(sourceWalletId, operation, createdAt)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Returns the total amount received from transfer operations by a wallet
     * before the given timestamp.
     *
     * @param sourceWalletId the ID of the wallet that received the transfers
     * @param createdAt      the timestamp threshold; only transactions before this
     *                       will be summed
     * @return the total amount of received transfers or {@code BigDecimal.ZERO} if
     *         none are found
     */
    public BigDecimal sumReceivedTransfersBefore(@Positive long sourceWalletId, @NotNull LocalDateTime createdAt) {
        return repository.sumReceivedTransfers(sourceWalletId, OperationType.TRANSFER, createdAt)
                .orElse(BigDecimal.ZERO);
    }
}
