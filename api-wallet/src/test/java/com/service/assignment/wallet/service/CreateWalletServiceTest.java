package com.service.assignment.wallet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.service.assignment.wallet.domain.User;
import com.service.assignment.wallet.domain.Wallet;
import com.service.assignment.wallet.exception.UserNotFoundException;
import com.service.assignment.wallet.repository.UserRepository;
import com.service.assignment.wallet.repository.WalletRepository;

@ExtendWith(MockitoExtension.class)
class CreateWalletServiceTest {

    @InjectMocks
    CreateWalletService createWalletService;

    @Mock
    WalletRepository walletRepository;

    @Mock
    UserRepository userRepository;

    @Test
    void execute_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        // Given
        long userId = 3L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> createWalletService.execute(userId));

        verify(userRepository).findById(userId);
        verifyNoInteractions(walletRepository);
    }

    @Test
    void execute_shouldReturnExistingWallet_whenWalletAlreadyExists() {
        // Given
        long userId = 1L;
        User user = new User(userId, "anyemail@test.com");

        Wallet existingWallet = Wallet.of(user, BigDecimal.TEN);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(existingWallet));

        // When
        Wallet result = createWalletService.execute(userId);

        // Then
        assertNotNull(result);
        assertEquals(existingWallet, result);

        verify(walletRepository).findByUserId(userId);
        verify(walletRepository, never()).saveAndFlush(any());
    }

    @Test
    void execute_shouldCreateNewWallet_whenUserExistsAndNoExistingWallet() {
        // Given
        long userId = 1L;
        User user = new User(userId, "anyemail@test.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(walletRepository.findByUserId(userId)).thenReturn(Optional.empty());

        Wallet savedWallet = Wallet.of(user, BigDecimal.ZERO);
        when(walletRepository.saveAndFlush(any(Wallet.class))).thenReturn(savedWallet);

        // When
        Wallet result = createWalletService.execute(userId);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getUser().getId());
        assertEquals(BigDecimal.ZERO, result.getBalance());

        verify(userRepository).findById(userId);
        verify(walletRepository).findByUserId(userId);
        verify(walletRepository).saveAndFlush(savedWallet);
    }
}
