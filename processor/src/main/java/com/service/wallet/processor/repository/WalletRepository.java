package com.service.wallet.processor.repository;

import com.service.wallet.processor.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByCustomerId(long userId);
}
