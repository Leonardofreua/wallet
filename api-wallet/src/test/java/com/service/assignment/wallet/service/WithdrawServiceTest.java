package com.service.assignment.wallet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.service.assignment.wallet.bean.WithdrawBean;
import com.service.assignment.wallet.domain.User;
import com.service.assignment.wallet.domain.Wallet;
import com.service.assignment.wallet.domain.type.Amount;
import com.service.assignment.wallet.exception.WalletNotFoundException;
import com.service.assignment.wallet.exception.WithdrawException;
import com.service.assignment.wallet.repository.WalletRepository;

@ExtendWith(MockitoExtension.class)
class WithdrawServiceTest {

    @Mock
    WalletRepository walletRepository;

    @Mock
    TransactionLogService transactionLogService;

    @InjectMocks
    WithdrawService withdrawService;

    @Test
    void execute_shouldThrowWalletNotFoundException_whenWalletDoesNotExist() {
        // Given
        long walletId = 1L;
        var withdrawBean = new WithdrawBean(walletId, 10L, new Amount(BigDecimal.TEN));
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(WalletNotFoundException.class, () -> withdrawService.execute(withdrawBean));

        verify(walletRepository).findById(walletId);
        verifyNoMoreInteractions(walletRepository);
        verifyNoInteractions(transactionLogService);
    }

    @Test
    void execute_shouldThrowWithdrawException_whenUserDoesNotOwnTheWallet() {
        // Given
        long walletId = 1L;
        long correctUserId = 100L;
        long incorrectUserId = 999L;
        var wallet = Wallet.of(new User(correctUserId, "email@test.com"), BigDecimal.valueOf(100));
        var withdrawBean = new WithdrawBean(walletId, incorrectUserId, new Amount(BigDecimal.TEN));
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        // When & Then
        var exception = assertThrows(WithdrawException.class, () -> withdrawService.execute(withdrawBean));

        assertEquals("The user can only withdraw from his own wallet!", exception.getMessage());

        verify(walletRepository).findById(walletId);
        verifyNoInteractions(transactionLogService);
    }

    @Test
    void execute_shouldThrowWithdrawException_whenWalletBalanceIsZero() {
        // Given
        long userId = 100L;
        long walletId = 1L;
        var wallet = Wallet.of(new User(userId, "email@test.com"), BigDecimal.ZERO);
        var withdrawBean = new WithdrawBean(walletId, userId, new Amount(BigDecimal.TEN));
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        // When & Then
        var exception = assertThrows(WithdrawException.class, () -> withdrawService.execute(withdrawBean));

        assertEquals("Wallet without balance!", exception.getMessage());

        verify(walletRepository).findById(walletId);
        verifyNoInteractions(transactionLogService);
    }

    @Test
    void execute_shouldThrowWithdrawException_whenRequestedAmountIsGreaterThanBalance() {
        // Given
        long userId = 100L;
        long walletId = 1L;
        var wallet = Wallet.of(new User(userId, "email@test.com"), BigDecimal.TEN);
        var withdrawBean = new WithdrawBean(walletId, userId, new Amount(BigDecimal.valueOf(20)));
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        // When & Then
        var exception = assertThrows(WithdrawException.class, () -> withdrawService.execute(withdrawBean));

        assertEquals("The amount to be withdrawn cannot be greater than the current balance!", exception.getMessage());

        verify(walletRepository).findById(walletId);
        verifyNoInteractions(transactionLogService);
    }

    @Test
    void execute_shouldWithdrawAmountSuccessfully_whenAllConditionsAreMet() {
        // Given
        long userId = 100L;
        long walletId = 1L;
        var initialBalance = BigDecimal.valueOf(100);
        var amountToWithdraw = BigDecimal.valueOf(30);
        var wallet = Wallet.of(new User(userId, "email@test.com"), initialBalance);
        var withdrawBean = new WithdrawBean(walletId, userId, new Amount(amountToWithdraw));

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        // When & Then
        withdrawService.execute(withdrawBean);

        assertEquals(initialBalance.subtract(amountToWithdraw), wallet.getBalance());
        verify(walletRepository).findById(walletId);
        verify(walletRepository).save(wallet);
        // verify(transactionLogService)
        //         .log(TransactionLogBean.of(wallet, withdrawBean.amount(), OperationType.WITHDRAW));
    }
}
