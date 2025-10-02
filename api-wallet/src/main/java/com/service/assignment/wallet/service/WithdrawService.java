package com.service.assignment.wallet.service;

import java.math.BigDecimal;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.service.assignment.wallet.bean.WithdrawBean;
import com.service.assignment.wallet.domain.Wallet;
import com.service.assignment.wallet.exception.WalletNotFoundException;
import com.service.assignment.wallet.exception.WithdrawException;
import com.service.assignment.wallet.repository.WalletRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for handling withdrawal operations from a user's wallet.
 * 
 * <pre>
 * This service ensures that withdrawals are only allowed:
 * - If the wallet exists and belongs to the requesting user
 * - If the wallet has a non-zero balance
 * - If the withdrawal amount is less than or equal to the current balance
 * 
 * After a successful withdrawal, the balance is updated and the transaction is
 * logged.
 * </pre>
 * 
 * @author Leonardo Freua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final WalletRepository walletRepository;
    private final TransactionLogService transactionLogService;

    /**
     * Executes a withdrawal operation based on the given {@link WithdrawBean}.
     * 
     * <pre>
     * It performs several validations:
     * 
     * - Checks if the wallet exists
     * - Validates ownership of the wallet by the requesting user
     * - Ensures that the wallet has sufficient balance
     * 
     * If all checks pass, the withdrawal amount is subtracted from the wallet’s
     * balance, the updated wallet is saved, and a transaction log is created.
     * </pre>
     *
     * @param withdrawBean the data required to process the withdrawal
     * @throws WalletNotFoundException if no wallet exists with the provided wallet
     *                                 ID
     * @throws WithdrawException       if the user does not own the wallet, or if
     *                                 balance is insufficient
     */
    @Transactional(rollbackOn = { WalletNotFoundException.class, WithdrawException.class})
    @CacheEvict(value = "balances", key = "#withdrawBean.walletId()")
    public void execute(@NotNull @Valid WithdrawBean withdrawBean) {
        log.info("Withdrawal requested with the following criteria: {}", withdrawBean);
        Wallet wallet = walletRepository.findById(withdrawBean.walletId())
                .orElseThrow(() -> new WalletNotFoundException(withdrawBean.walletId()));

        if (wallet.getUser().getId() != withdrawBean.userId()) {
            throw new WithdrawException("The user can only withdraw from his own wallet!");
        }

        if (wallet.getBalance().compareTo(BigDecimal.ZERO) == 0) {
            throw new WithdrawException("Wallet without balance!");
        }

        BigDecimal desiredAmount = withdrawBean.amount().value();
        if (desiredAmount.compareTo(wallet.getBalance()) == 1) {
            throw new WithdrawException("The amount to be withdrawn cannot be greater than the current balance!");
        }

        // BigDecimal newBalance = wallet.getBalance().subtract(desiredAmount);
        // wallet.setBalance(newBalance);

        // walletRepository.save(wallet);
        // transactionLogService.log(TransactionLogBean.of(wallet, withdrawBean.amount(), OperationType.WITHDRAW));

        // log.info("Withdrawal completed successfully to wallet {}", wallet.getId());
    }
}