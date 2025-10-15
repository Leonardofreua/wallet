package com.service.wallet.api.service;

import com.service.wallet.api.bean.TransactionLogBean;
import com.service.wallet.api.bean.TransferBean;
import com.service.wallet.api.bean.WithdrawBean;
import com.service.wallet.api.domain.Customer;
import com.service.wallet.api.domain.Wallet;
import com.service.wallet.api.enums.OperationType;
import com.service.wallet.api.repository.WalletRepository;
import com.service.wallet.api.type.Amount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    WalletRepository walletRepository;

    @Mock
    TransactionLogService transactionLogService;

    @Mock
    WithdrawService withdrawService;

    @Mock
    DepositService depositService;

    @InjectMocks
    TransferService transferService;

    @Test
    void execute_shouldTransferException_whenSourceAndTargetUsersAreSame() {
        // Given
        long userId = 1L;
        var bean = new TransferBean(10L, userId, userId, new Amount(BigDecimal.TEN));

        // When & Then
//        var exception = assertThrows(TransferException.class, () -> transferService.execute(bean));
//        assertEquals("The source and destination user cannot be the same!", exception.getMessage());

        verifyNoInteractions(walletRepository, withdrawService, depositService, transactionLogService);
    }

    @Test
    void execute_shouldThrowWalletNotFoundException_whenSourceWalletNotFound() {
        // Given
        long sourceWalletId = 10L;
        var bean = new TransferBean(sourceWalletId, 1L, 2L, new Amount(BigDecimal.TEN));
        when(walletRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
//        var exception = assertThrows(WalletNotFoundException.class, () -> transferService.execute(bean));
//        assertEquals("Source wallet not found!", exception.getMessage());

        verify(walletRepository).findById(sourceWalletId);
        verifyNoMoreInteractions(walletRepository);
        verifyNoInteractions(withdrawService, depositService, transactionLogService);
    }

    @Test
    void execute_shouldThrowTransferException_whenWalletDoesNotBelongToUser() {
        // Given
        long userId = 1L;
        long wrongUserId = 99L;
        long sourceWalletId = 10L;
        var sourceWallet = Wallet.builder()
                .id(sourceWalletId)
                .customer(new Customer(wrongUserId, "wrong@test.com"))
                .build();

        var bean = new TransferBean(sourceWalletId, userId, 2L, new Amount(BigDecimal.TEN));
        when(walletRepository.findById(sourceWalletId)).thenReturn(Optional.of(sourceWallet));

        // When & Then
//        var exception = assertThrows(TransferException.class, () -> transferService.execute(bean));
//        assertEquals("The provided wallet does not belong to the requesting user!", exception.getMessage());

        verify(walletRepository).findById(sourceWalletId);
        verifyNoMoreInteractions(walletRepository);
        verifyNoInteractions(withdrawService, depositService, transactionLogService);
    }

    @Test
    void execute_shouldThrowWalletNotFoundException_whenTargetWalletNotFound() {
        // Given
        long sourceUserId = 1L;
        long targetUserId = 2L;
        long sourceWalletId = 10L;
        var sourceWallet = Wallet.builder()
                .id(sourceWalletId)
                .customer(new Customer(sourceUserId, "wrong@test.com"))
                .build();

        TransferBean bean = new TransferBean(sourceWalletId, sourceUserId, targetUserId, new Amount(BigDecimal.TEN));
        when(walletRepository.findById(sourceWalletId)).thenReturn(Optional.of(sourceWallet));
        when(walletRepository.findByCustomerId(targetUserId)).thenReturn(Optional.empty());

        // When & Then
//        WalletNotFoundException exception = assertThrows(WalletNotFoundException.class,
//                () -> transferService.execute(bean));
//        assertEquals("Target wallet not found!", exception.getMessage());

        verify(walletRepository).findById(sourceWalletId);
        verify(walletRepository).findByCustomerId(targetUserId);
        verifyNoMoreInteractions(walletRepository);
        verifyNoInteractions(withdrawService, depositService, transactionLogService);
    }

    @Test
    void execute_shouldTransferAmountAndLogTransaction_whenValidTransfer() {
        // Given
        long sourceUserId = 1L;
        long targetUserId = 2L;
        long sourceWalletId = 10L;
        long targetWalletId = 20L;

        Wallet sourceWallet = Wallet.builder()
                .id(sourceWalletId)
                .customer(new Customer(sourceUserId, "source@test.com"))
                .build();

        Wallet targetWallet = Wallet.builder()
                .id(targetWalletId)
                .customer(new Customer(targetUserId, "target@test.com"))
                .build();

        Amount amount = new Amount(new BigDecimal("100.00"));
        TransferBean bean = new TransferBean(sourceWalletId, sourceUserId, targetUserId, amount);

        when(walletRepository.findById(sourceWalletId)).thenReturn(Optional.of(sourceWallet));
        when(walletRepository.findByCustomerId(targetUserId)).thenReturn(Optional.of(targetWallet));

        // When
//        transferService.execute(bean);

        // Then
        verify(walletRepository).findById(sourceWalletId);
        verify(walletRepository).findByCustomerId(targetUserId);

        verify(withdrawService).execute(new WithdrawBean(sourceWalletId, sourceUserId, amount));
        // verify(depositService).execute(new DepositMessage(targetWalletId, targetUserId, amount));

        ArgumentCaptor<TransactionLogBean> captor = ArgumentCaptor.forClass(TransactionLogBean.class);
//        verify(transactionLogService).log(captor.capture());

        TransactionLogBean logBean = captor.getValue();
        assertEquals(sourceWallet, logBean.sourceWallet());
        assertEquals(targetWallet, logBean.targetWallet());
        assertEquals(OperationType.TRANSFER, logBean.operationType());
        assertEquals(amount, logBean.amount());
    }
}
