package com.service.assignment.wallet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import com.service.assignment.wallet.domain.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.service.assignment.wallet.domain.Wallet;
import com.service.assignment.wallet.exception.WalletNotFoundException;
import com.service.assignment.wallet.repository.WalletRepository;

@ExtendWith(MockitoExtension.class)
class WalletBalanceServiceTest {

    @Mock
    WalletRepository walletRepository;

    @InjectMocks
    WalletBalanceService walletBalanceService;

    @Test
    void execute_shouldThrowWalletNotFoundException_whenWalletDoesNotExist() {
        // Given
        long walletId = 1L;
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(WalletNotFoundException.class, () -> walletBalanceService.execute(walletId));
        verify(walletRepository).findById(walletId);
    }

    @Test
    void execute_shouldReturnBalance_whenWalletExists() {
        // Given
        long walletId = 2L;
        BigDecimal expectedBalance = new BigDecimal("100.00");
        var wallet = Wallet.of(new Customer(walletId, "email@test.com"), expectedBalance);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        // When & Then
        BigDecimal balance = walletBalanceService.execute(walletId);

        assertEquals(expectedBalance, balance);
        verify(walletRepository).findById(walletId);
    }
}
