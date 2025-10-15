package com.service.wallet.api.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.service.wallet.api.domain.TransactionLog;
import com.service.wallet.api.enums.OperationType;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {

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
