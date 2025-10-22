package com.service.wallet.processor.repository;

import com.service.wallet.processor.domain.TransactionLog;
import com.service.wallet.processor.enums.OperationType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {

    Optional<TransactionLog> findByCorrelationId(@NotNull  UUID correlationId);

    List<TransactionLog> findAllByCorrelationId(@NotNull  UUID correlationId);

    @Query("""
                SELECT SUM(t.amount) FROM TransactionLog t
                WHERE t.sourceWallet.id = :sourceWallet
                  AND t.operation = :operation
                  AND t.createdAt <= :createdAt
            """)
    Optional<BigDecimal> sumBySourceWalletIdAndTypeBefore(
            @Param("sourceWallet") long sourceWallet,
            @Param("operation") OperationType operation,
            @Param("createdAt") LocalDateTime createdAt);

    @Query("""
                SELECT SUM(t.amount) FROM TransactionLog t
                WHERE t.targetWallet.id = :sourceWallet
                  AND t.operation = :operation
                  AND t.createdAt <= :createdAt
            """)
    Optional<BigDecimal> sumReceivedTransfers(
            @Param("sourceWallet") long sourceWallet,
            @Param("operation") OperationType operation,
            @Param("createdAt") LocalDateTime createdAt);

}
