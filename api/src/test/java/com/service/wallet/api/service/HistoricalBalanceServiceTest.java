package com.service.wallet.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.service.wallet.api.enums.OperationType;

@ExtendWith(MockitoExtension.class)
class HistoricalBalanceServiceTest {

    @Mock
    TransactionLogService transactionLogService;

    @InjectMocks
    HistoricalBalanceService historicalBalanceService;

    @Test
    void getHistoricalBalance_shouldReturnZero_whenAllOperationsAreZero() {
        // Given
        long walletId = 1L;
        LocalDateTime date = LocalDateTime.now();

        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.DEPOSIT, date))
                .thenReturn(BigDecimal.ZERO);
        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.WITHDRAW, date))
                .thenReturn(BigDecimal.ZERO);
        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.TRANSFER, date))
                .thenReturn(BigDecimal.ZERO);
        when(transactionLogService.sumReceivedTransfersBefore(walletId, date))
                .thenReturn(BigDecimal.ZERO);

        // When & Then
        BigDecimal balance = historicalBalanceService.getHistoricalBalance(walletId, date);
        assertEquals(BigDecimal.ZERO, balance);

        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.DEPOSIT, date);
        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.WITHDRAW, date);
        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.TRANSFER, date);
        verify(transactionLogService).sumReceivedTransfersBefore(walletId, date);
    }

    @Test
    void getHistoricalBalance_shouldReturnCorrectBalance_whenOperationsHaveValues() {
        // Given
        long walletId = 2L;
        LocalDateTime date = LocalDateTime.now();

        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.DEPOSIT, date))
                .thenReturn(new BigDecimal("100.00"));
        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.WITHDRAW, date))
                .thenReturn(new BigDecimal("30.00"));
        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.TRANSFER, date))
                .thenReturn(new BigDecimal("20.00")); // Sent
        when(transactionLogService.sumReceivedTransfersBefore(walletId, date))
                .thenReturn(new BigDecimal("50.00")); // Received

        // When & Then
        BigDecimal balance = historicalBalanceService.getHistoricalBalance(walletId, date);
        // Expected: 100 + 50 - 30 - 20 = 100
        assertEquals(new BigDecimal("100.00"), balance);

        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.DEPOSIT, date);
        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.WITHDRAW, date);
        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.TRANSFER, date);
        verify(transactionLogService).sumReceivedTransfersBefore(walletId, date);
    }

    @Test
    void getHistoricalBalance_shouldReturnOnlyDeposits_whenOtherOperationsAreZero() {
        // Given
        long walletId = 1L;
        LocalDateTime date = LocalDateTime.now();

        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.DEPOSIT, date))
                .thenReturn(new BigDecimal("200.00"));
        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.WITHDRAW, date))
                .thenReturn(BigDecimal.ZERO);
        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.TRANSFER, date))
                .thenReturn(BigDecimal.ZERO);
        when(transactionLogService.sumReceivedTransfersBefore(walletId, date))
                .thenReturn(BigDecimal.ZERO);

        // When & Then
        BigDecimal balance = historicalBalanceService.getHistoricalBalance(walletId, date);
        assertEquals(new BigDecimal("200.00"), balance);

        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.DEPOSIT, date);
        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.WITHDRAW, date);
        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.TRANSFER, date);
        verify(transactionLogService).sumReceivedTransfersBefore(walletId, date);
    }

    @Test
    void getHistoricalBalance_shouldSubtractWithdrawalsOnly_whenOtherOperationsAreZero() {
        // Given
        long walletId = 2L;
        LocalDateTime date = LocalDateTime.now();

        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.DEPOSIT, date))
                .thenReturn(BigDecimal.ZERO);
        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.WITHDRAW, date))
                .thenReturn(new BigDecimal("50.00"));
        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.TRANSFER, date))
                .thenReturn(BigDecimal.ZERO);
        when(transactionLogService.sumReceivedTransfersBefore(walletId, date))
                .thenReturn(BigDecimal.ZERO);

        // When & Then
        BigDecimal balance = historicalBalanceService.getHistoricalBalance(walletId, date);
        assertEquals(new BigDecimal("-50.00"), balance);

        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.DEPOSIT, date);
        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.WITHDRAW, date);
        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.TRANSFER, date);
        verify(transactionLogService).sumReceivedTransfersBefore(walletId, date);
    }

    @Test
    void getHistoricalBalance_shouldSubtractTransfersSentOnly_whenOtherOperationsAreZero() {
        // Given
        long walletId = 3L;
        LocalDateTime date = LocalDateTime.now();

        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.DEPOSIT, date))
                .thenReturn(BigDecimal.ZERO);
        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.WITHDRAW, date))
                .thenReturn(BigDecimal.ZERO);
        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.TRANSFER, date))
                .thenReturn(new BigDecimal("40.00"));
        when(transactionLogService.sumReceivedTransfersBefore(walletId, date))
                .thenReturn(BigDecimal.ZERO);

        // When & Then
        BigDecimal balance = historicalBalanceService.getHistoricalBalance(walletId, date);
        assertEquals(new BigDecimal("-40.00"), balance);

        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.DEPOSIT, date);
        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.WITHDRAW, date);
        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.TRANSFER, date);
        verify(transactionLogService).sumReceivedTransfersBefore(walletId, date);
    }

    @Test
    void getHistoricalBalance_shouldAddTransfersReceivedOnly_whenOtherOperationsAreZero() {
        // Given
        long walletId = 4L;
        LocalDateTime date = LocalDateTime.now();

        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.DEPOSIT, date))
                .thenReturn(BigDecimal.ZERO);
        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.WITHDRAW, date))
                .thenReturn(BigDecimal.ZERO);
        when(transactionLogService.sumAmountByOperationBefore(walletId, OperationType.TRANSFER, date))
                .thenReturn(BigDecimal.ZERO);
        when(transactionLogService.sumReceivedTransfersBefore(walletId, date))
                .thenReturn(new BigDecimal("70.00"));

        // When & Then
        BigDecimal balance = historicalBalanceService.getHistoricalBalance(walletId, date);
        assertEquals(new BigDecimal("70.00"), balance);

        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.DEPOSIT, date);
        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.WITHDRAW, date);
        verify(transactionLogService).sumAmountByOperationBefore(walletId, OperationType.TRANSFER, date);
        verify(transactionLogService).sumReceivedTransfersBefore(walletId, date);
    }
}
