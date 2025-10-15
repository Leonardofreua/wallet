package com.service.wallet.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.wallet.api.domain.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByCustomerId(long userId);
}
