package com.service.wallet.api.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.service.wallet.api.repository.WalletRepository;

@ExtendWith(MockitoExtension.class)
class DepositServiceTest {

    @InjectMocks
    DepositService depositService;

    @Mock
    WalletRepository walletRepository;

    @Mock
    TransactionLogService transactionLogService;

    // @Test
    // void execute_shouldThrowWalletNotFoundException_whenWalletDoesNotExist() {
    //     // Given
    //     var depositBean = new DepositMessage(1L, 100L, new Amount(BigDecimal.TEN));

    //     when(walletRepository.findById(anyLong())).thenReturn(Optional.empty());

    //     // When & Then
    //     // assertThrows(WalletNotFoundException.class, () -> depositService.execute(depositBean));

    //     verify(walletRepository).findById(depositBean.walletId());
    //     verify(walletRepository, never()).save(any());
    //     verifyNoInteractions(transactionLogService);
    // }

    // @Test
    // void execute_shouldThrowDepositException_whenUserDoesNotMatchWalletUser() {
    //     // Given
    //     long walletId = 1L;
    //     long walletUserId = 999L;
    //     long depositUserId = 100L;

    //     var depositBean = new DepositMessage(walletId, depositUserId, new Amount(BigDecimal.TEN));
    //     var walletUser = new User(walletUserId, "wallet-owner@test.com");
    //     var wallet = Wallet.builder()
    //             .id(walletId)
    //             .user(walletUser)
    //             .balance(BigDecimal.valueOf(100))
    //             .build();

    //     when(walletRepository.findById(anyLong())).thenReturn(Optional.of(wallet));

    //     // When & Then
    //     // var exception = assertThrows(DepositException.class, () -> depositService.execute(depositBean));
    //     // assertEquals("The user can only deposit into his own wallet!", exception.getMessage());

    //     verify(walletRepository).findById(depositBean.walletId());
    //     verify(walletRepository, never()).save(any());
    //     verifyNoInteractions(transactionLogService);
    // }

    // @Test
    // void execute_shouldDepositAmount_whenWalletExistsAndUserMatches() {
    //     // Given
    //     long walletId = 1L;
    //     long userId = 100L;

    //     var depositBean = new DepositMessage(walletId, userId, new Amount(BigDecimal.valueOf(50)));
    //     var user = new User(userId, "user@example.com");
    //     var wallet = Wallet.builder()
    //             .id(walletId)
    //             .user(user)
    //             .balance(BigDecimal.valueOf(100))
    //             .build();

    //     when(walletRepository.findById(anyLong())).thenReturn(Optional.of(wallet));
    //     when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

    //     // When
    //     // depositService.execute(depositBean);

    //     // Then
    //     assertEquals(BigDecimal.valueOf(150), wallet.getBalance());

    //     verify(walletRepository).findById(depositBean.walletId());
    //     verify(walletRepository).save(wallet);

    //     ArgumentCaptor<TransactionLogBean> transactionLogBeanCaptor = ArgumentCaptor.forClass(TransactionLogBean.class);
    //     verify(transactionLogService).log(transactionLogBeanCaptor.capture());

    //     var transactionLogBean = transactionLogBeanCaptor.getValue();
    //     assertEquals(transactionLogBean.sourceWallet(), wallet);
    //     assertEquals(transactionLogBean.amount(), depositBean.amount());
    //     assertEquals(transactionLogBean.operationType(), OperationType.DEPOSIT);
    // }
}
