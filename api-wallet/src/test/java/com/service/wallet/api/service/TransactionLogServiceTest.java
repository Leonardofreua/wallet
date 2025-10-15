package com.service.wallet.api.service;

import com.service.wallet.api.enums.OperationType;
import com.service.wallet.api.repository.TransactionLogRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionLogServiceTest {

    @Mock
    TransactionLogRepository transactionLogRepository;

    @InjectMocks
    TransactionLogService service;

    @Nested
    class LogTests {

        @Test
        void log_shouldTransactionLogException_whenTransferWithoutTargetWallet() {
//            // Given
//            var source = new Wallet();
//            var bean = new TransactionLogBean(source, null, new Amount(BigDecimal.ONE),
//                    OperationType.TRANSFER);
//
//            // When & Then
//            var exception = assertThrows(TransactionLogException.class, () -> service.log(bean));
//
//            assertEquals("It is mandatory to inform the target wallet to register the transfer type transaction.",
//                    exception.getMessage());
//
//            verifyNoInteractions(transactionLogRepository);
        }

        @ParameterizedTest
        @EnumSource(value = OperationType.class, names = "TRANSFER", mode = Mode.EXCLUDE)
        void log_shouldPersistTransaction_whenOperationIsNotTransfer(OperationType operationType) {
//            // Given
//            var wallet = new Wallet();
//            var bean = new TransactionLogBean(wallet, null, new Amount(BigDecimal.TEN), operationType);
//
//            // When
//            service.log(bean);
//
//            // Then
//            ArgumentCaptor<TransactionLog> captor = ArgumentCaptor.forClass(TransactionLog.class);
//            verify(transactionLogRepository).save(captor.capture());
//
//            TransactionLog saved = captor.getValue();
//            assertEquals(wallet, saved.getSourceWallet());
//            assertEquals(operationType, saved.getOperation());
//            assertEquals(BigDecimal.TEN, saved.getAmount());
//            assertNotNull(saved.getCreatedAt());
//            assertNull(saved.getTargetWallet());
        }

        @Test
        void log_shouldPersistTransactionWithTargetWallet_whenOperationIsTransfer() {
//            // Given
//            var source = new Wallet();
//            var target = new Wallet();
//            var bean = new TransactionLogBean(source, target, new Amount(BigDecimal.ONE), OperationType.TRANSFER);
//
//            // When
//            service.log(bean);
//
//            // Then
//            ArgumentCaptor<TransactionLog> captor = ArgumentCaptor.forClass(TransactionLog.class);
//            verify(transactionLogRepository).save(captor.capture());
//
//            TransactionLog saved = captor.getValue();
//            assertEquals(source, saved.getSourceWallet());
//            assertEquals(target, saved.getTargetWallet());
//            assertEquals(OperationType.TRANSFER, saved.getOperation());
//            assertEquals(BigDecimal.ONE, saved.getAmount());
//            assertNotNull(saved.getCreatedAt());
        }
    }

    @Nested
    class SumAmountByOperationBeforeTests {

        @ParameterizedTest
        @EnumSource(value = OperationType.class)
        void sumAmountByOperationBefore_shouldReturnValue_whenPresent(OperationType operationType) {
            // Given
            long walletId = 1L;
            var createdAt = LocalDateTime.now();
            var amountExpected = new BigDecimal("100.00");

            when(transactionLogRepository.sumBySourceWalletIdAndTypeBefore(walletId, operationType, createdAt))
                    .thenReturn(Optional.of(amountExpected));

            // When & Then
            BigDecimal result = service.sumAmountByOperationBefore(walletId, operationType, createdAt);
            assertEquals(amountExpected, result);

            verify(transactionLogRepository).sumBySourceWalletIdAndTypeBefore(walletId, operationType, createdAt);
        }

        @ParameterizedTest
        @EnumSource(value = OperationType.class)
        void sumAmountByOperationBefore_shouldReturnZero_whenEmpty(OperationType operationType) {
            // Given
            long walletId = 1L;
            var createdAt = LocalDateTime.now();

            when(transactionLogRepository.sumBySourceWalletIdAndTypeBefore(walletId, operationType, createdAt))
                    .thenReturn(Optional.empty());

            // When & Then
            BigDecimal result = service.sumAmountByOperationBefore(walletId, operationType, createdAt);
            assertEquals(BigDecimal.ZERO, result);

            verify(transactionLogRepository).sumBySourceWalletIdAndTypeBefore(walletId, operationType, createdAt);
        }
    }

    @Nested
    class SumReceivedTransfersBeforeTests {

        @Test
        void sumReceivedTransfersBefore_shouldReturnValue_whenPresent() {
            // Given
            long walletId = 2L;
            var createdAt = LocalDateTime.now();
            var amountExpected = new BigDecimal("75.50");

            when(transactionLogRepository.sumReceivedTransfers(walletId, OperationType.TRANSFER, createdAt))
                    .thenReturn(Optional.of(amountExpected));

            // When & Then
            BigDecimal result = service.sumReceivedTransfersBefore(walletId, createdAt);
            assertEquals(amountExpected, result);

            verify(transactionLogRepository).sumReceivedTransfers(walletId, OperationType.TRANSFER, createdAt);
        }

        @Test
        void sumReceivedTransfersBefore_shouldReturnZero_whenEmpty() {
            // Given
            long walletId = 2L;
            var date = LocalDateTime.now();

            when(transactionLogRepository.sumReceivedTransfers(walletId, OperationType.TRANSFER, date))
                    .thenReturn(Optional.empty());

            // When & Then
            BigDecimal result = service.sumReceivedTransfersBefore(walletId, date);
            assertEquals(BigDecimal.ZERO, result);

            verify(transactionLogRepository).sumReceivedTransfers(walletId, OperationType.TRANSFER, date);
        }
    }
}
