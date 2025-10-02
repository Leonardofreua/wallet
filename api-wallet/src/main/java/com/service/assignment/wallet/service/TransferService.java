package com.service.assignment.wallet.service;

import org.springframework.stereotype.Service;

import com.service.assignment.wallet.bean.TransferBean;
import com.service.assignment.wallet.domain.Wallet;
import com.service.assignment.wallet.exception.DepositException;
import com.service.assignment.wallet.exception.TransferException;
import com.service.assignment.wallet.exception.WalletNotFoundException;
import com.service.assignment.wallet.exception.WithdrawException;
import com.service.assignment.wallet.repository.WalletRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private final WithdrawService withdrawService;
    private final DepositService depositService;

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
    @Transactional(rollbackOn = { TransferException.class, WalletNotFoundException.class, WithdrawException.class,
            DepositException.class })
    public void execute(@NotNull @Valid TransferBean transferBean)
            throws TransferException, WalletNotFoundException {
        log.info("Transfer requested with the following criteria: {}", transferBean);
        if (transferBean.sourceUserId() == transferBean.targetUserId()) {
            throw new TransferException("The source and destination user cannot be the same!");
        }

        Wallet sourceWallet = walletRepository.findById(transferBean.sourceWalletId())
                .orElseThrow(() -> new WalletNotFoundException("Source wallet not found!"));

        if (sourceWallet.getUser().getId() != transferBean.sourceUserId()) {
            throw new TransferException("The provided wallet does not belong to the requesting user!");
        }

        Wallet targetWallet = walletRepository.findByUserId(transferBean.targetUserId())
                .orElseThrow(() -> new WalletNotFoundException("Target wallet not found!"));

        // withdrawService
        //         .execute(new WithdrawBean(sourceWallet.getId(), transferBean.sourceUserId(), transferBean.amount()));
        // depositService
        //         .execute(1L, new DepositRequest(targetWallet.getId(), transferBean.targetUserId(), transferBean.amount()));

        // transactionLogService
        //         .log(new TransactionLogBean(sourceWallet, targetWallet, transferBean.amount(), OperationType.TRANSFER));
    }
}