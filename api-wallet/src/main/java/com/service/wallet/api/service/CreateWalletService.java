package com.service.wallet.api.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.service.wallet.api.domain.Wallet;
import com.service.wallet.api.exception.UserNotFoundException;
import com.service.wallet.api.repository.UserRepository;
import com.service.wallet.api.repository.WalletRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for handling the creation of wallets for users.
 * <p>
 * This service ensures that each user has at most one wallet. If a wallet
 * already exists
 * for the given user ID, it will return the existing wallet instead of creating
 * a new one.
 * </p>
 *
 * <p>
 * The wallet is initialized with a balance of zero.
 * </p>
 *
 * @author Leonardo Freua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateWalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    /**
     * Creates a wallet for the specified user if one does not already exist.
     * <p>
     * If a wallet already exists for the given user ID, it returns the existing
     * wallet.
     * If the user does not exist, a {@link UserNotFoundException} is thrown.
     * </p>
     *
     * @param userId the ID of the user for whom the wallet should be created
     * @return the existing or newly created {@link Wallet} instance
     * @throws UserNotFoundException if no user exists with the given ID
     */
    @Transactional
    public Wallet execute(@Positive long userId) {
        log.info("Requested to create a wallet for the user {}", userId);
        var customer = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        var existingWallet = walletRepository.findByCustomerId(userId);
        if (existingWallet.isPresent()) {
            log.info("User {} already has a wallet.", userId);
            return existingWallet.get();
        }

        Wallet newWallet = Wallet.of(customer, BigDecimal.ZERO);
        walletRepository.saveAndFlush(newWallet);

        log.info("Wallet successfully created for user {}", userId);

        return newWallet;
    }
}
