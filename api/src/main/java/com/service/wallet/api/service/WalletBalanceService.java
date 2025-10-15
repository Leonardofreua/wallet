package com.service.wallet.api.service;

import java.math.BigDecimal;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.service.wallet.api.domain.Wallet;
import com.service.wallet.api.exception.WalletNotFoundException;
import com.service.wallet.api.repository.WalletRepository;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for retrieving the current balance of a wallet.
 * <p>
 * This service provides a simple read operation that returns the current
 * balance for a wallet identified by its ID. If the wallet is not found, a
 * {@link WalletNotFoundException} is thrown.
 * </p>
 *
 * @author Leonardo Freua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletBalanceService {

    private final WalletRepository walletRepository;

    /**
     * Retrieves the current balance of the wallet associated with the given ID.
     *
     * @param walletId the unique identifier of the wallet
     * @return the current balance of the wallet
     * @throws WalletNotFoundException if no wallet is found with the specified ID
     */
    @Cacheable(value = "balances", key = "#walletId")
    public BigDecimal execute(@Positive long walletId) {
        log.info("Request current balance for wallet {}", walletId);
        return walletRepository.findById(walletId)
                .map(Wallet::getBalance)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
    }
}
