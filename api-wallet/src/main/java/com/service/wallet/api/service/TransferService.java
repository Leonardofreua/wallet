package com.service.wallet.api.service;

import com.service.wallet.api.bean.TransactionLogBean;
import com.service.wallet.api.domain.Wallet;
import com.service.wallet.api.dto.api.request.TransferRequest;
import com.service.wallet.api.dto.messaging.TransactionCorrelationId;
import com.service.wallet.api.enums.TransactionStatus;
import com.service.wallet.api.exception.TransferException;
import com.service.wallet.api.exception.WalletNotFoundException;
import com.service.wallet.api.queue.producer.TransferProducer;
import com.service.wallet.api.repository.WalletRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service responsible for handling wallet transfer operations between two
 * different users.
 *
 * <pre>
 * This service ensures the following:
 *
 *   - The source and target users are not the same
 *   - The source wallet exists and belongs to the requesting user
 *   - The target wallet exists and belongs to a different user
 *   - A withdrawal is performed from the source wallet
 *   - A deposit is performed into the target wallet
 *   - The transfer is logged for audit and traceability purposes
 * </pre>
 *
 * <p>
 * All operations are performed within a transactional boundary to ensure
 * atomicity.
 * </p>
 *
 * @author Leonardo Freua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

    private final WalletRepository walletRepository;
    private final TransactionLogService transactionLogService;
    private final TransferProducer transferProducer;

    /**
     * Executes a money transfer between two wallets owned by different users.
     *
     * @param transferBean the details of the transfer operation, including source
     *                     and target wallets, user IDs, and the amount to be
     *                     transferred
     * @throws TransferException       if the source and target users are the same,
     *                                 if the source wallet doesn't
     *                                 belong to the user, or if any validation
     *                                 fails
     * @throws WalletNotFoundException if either the source or target wallet cannot
     *                                 be found
     */
    public void execute(@Positive long sourceWalletId, @NotNull @Valid TransferRequest transferRequest)
            throws TransferException, WalletNotFoundException {
        log.info("Transfer requested with the following criteria: {}", transferRequest);
        if (transferRequest.sourceUserId() == transferRequest.targetUserId()) {
            throw new TransferException("The source and destination user cannot be the same!");
        }

        Wallet sourceWallet = walletRepository.findById(sourceWalletId)
                .orElseThrow(() -> new WalletNotFoundException("Source wallet not found!"));

        if (sourceWallet.getCustomer().getId() != transferRequest.sourceUserId()) {
            throw new TransferException("The provided wallet does not belong to the requesting user!");
        }

        Wallet targetWallet = walletRepository.findByCustomerId(transferRequest.targetUserId())
                .orElseThrow(() -> new WalletNotFoundException("Target wallet not found!"));

        UUID transactionCorrelatiUuid = UUID.randomUUID();
        transactionLogService.log(
                TransactionLogBean.buildWithdraw(sourceWallet, transferRequest.amount(), transactionCorrelatiUuid), TransactionStatus.PROCESSING);
        transactionLogService.log(
                TransactionLogBean.buildDeposit(targetWallet, transferRequest.amount(), transactionCorrelatiUuid), TransactionStatus.PROCESSING);
        transactionLogService.log(
                TransactionLogBean.buildTransfer(sourceWallet, targetWallet, transferRequest.amount(), transactionCorrelatiUuid), TransactionStatus.PROCESSING);

        transferProducer.send(new TransactionCorrelationId(transactionCorrelatiUuid));
    }
}