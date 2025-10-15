package com.service.wallet.api.service;

import com.service.wallet.api.bean.TransactionLogBean;
import com.service.wallet.api.domain.Wallet;
import com.service.wallet.api.dto.api.request.DepositRequest;
import com.service.wallet.api.dto.messaging.TransactionCorrelationId;
import com.service.wallet.api.enums.TransactionStatus;
import com.service.wallet.api.exception.DepositException;
import com.service.wallet.api.exception.WalletNotFoundException;
import com.service.wallet.api.queue.producer.DepositProducer;
import com.service.wallet.api.repository.WalletRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service responsible for handling deposit operations into a user's wallet.
 *
 * <pre>
 * This service ensures that a user can only deposit funds into their own wallet.
 * Upon a successful deposit, the wallet balance is updated and the transaction is logged.
 *
 * This service is transactional, meaning the database operations within the method
 * will either complete successfully together or be rolled back in case of failure.
 * </pre>
 *
 * @author Leonardo Freua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {

    private final WalletRepository walletRepository;
    private final TransactionLogService transactionLogService;
    private final DepositProducer depositProducer;

    /**
     * Executes a deposit operation into a wallet as described by the provided
     * {@link DepositMessage}.
     *
     * <pre>
     * This method performs the following validations and steps:
     *   - Validates that the wallet exists
     *   - Ensures the deposit is being made by the wallet's owner
     *   - Adds the deposit amount to the current balance
     *   - Saves the updated wallet
     *   - Logs the transaction in the transaction log
     * </pre>
     *
     * @param depositBean the details of the deposit operation, including wallet ID,
     *                    user ID, and amount
     * @throws WalletNotFoundException if the wallet does not exist
     * @throws DepositException        if the wallet does not belong to the given
     *                                 user
     */
    // @CacheEvict(value = "balances", key = "#depositBean.walletId()") // TODO
    // remover o cache somente quando receber a mensagem de retorno via SNS
    public void execute(@Positive long targetWalletId, @NotNull @Valid DepositRequest depositRequest)
            throws DepositException {
        log.info("Deposit requested with the following criteria: {}", depositRequest);
        Wallet targetWallet = walletRepository.findById(targetWalletId)
                .orElseThrow(() -> new WalletNotFoundException(targetWalletId));

        if (targetWallet.getCustomer().getId() != depositRequest.userId()) {
            throw new DepositException("The user can only deposit into his own wallet!");
        }

        var transactionLog = transactionLogService
                .log(TransactionLogBean.buildDeposit(targetWallet, depositRequest.amount(), UUID.randomUUID()), TransactionStatus.PROCESSING);
        depositProducer.send(new TransactionCorrelationId(transactionLog.getCorrelationId()));

        // var newBalance = wallet.getBalance().add(depositBean.amount().value());
        // wallet.setBalance(newBalance);

        // walletRepository.save(wallet);
        // transactionLogService.log(TransactionLogBean.of(wallet, depositBean.amount(),
        // OperationType.DEPOSIT));

        // log.info("Deposit of {} completed in wallet {} belonging to user {}",
        // depositBean.amount(),
        // depositBean.walletId(), depositBean.userId());
    }
}
