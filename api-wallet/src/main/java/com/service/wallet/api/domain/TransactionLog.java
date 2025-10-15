package com.service.wallet.api.domain;

import com.service.wallet.api.enums.OperationType;
import com.service.wallet.api.enums.TransactionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction_log", schema = "wallet")
public class TransactionLog {

    @Id
    @SequenceGenerator(name = "transaction_log_id_seq", sequenceName = "transaction_log_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "transaction_log_id_seq", strategy = GenerationType.SEQUENCE)
    @Column(unique = true, nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "correlation_id", nullable = false, updatable = false)
    private UUID correlationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_wallet_id")
    private Wallet sourceWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_wallet_id")
    private Wallet targetWallet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType operation;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status", nullable = false)
    private TransactionStatus currentStatus;

    @Column(name = "error_message", length = 255)
    private String errorMessage;

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
