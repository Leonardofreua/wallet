package com.service.assignment.wallet.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.assignment.wallet.domain.Wallet;
import com.service.assignment.wallet.dto.api.request.CreateWalletRequest;
import com.service.assignment.wallet.dto.api.request.DepositRequest;
import com.service.assignment.wallet.dto.api.request.TransferRequest;
import com.service.assignment.wallet.dto.api.request.WithdrawRequest;
import com.service.assignment.wallet.dto.api.response.WalletResponse;
import com.service.assignment.wallet.mapper.TransferMapper;
import com.service.assignment.wallet.mapper.WalletMapper;
import com.service.assignment.wallet.mapper.WithdrawMapper;
import com.service.assignment.wallet.service.CreateWalletService;
import com.service.assignment.wallet.service.DepositService;
import com.service.assignment.wallet.service.HistoricalBalanceService;
import com.service.assignment.wallet.service.TransferService;
import com.service.assignment.wallet.service.WalletBalanceService;
import com.service.assignment.wallet.service.WithdrawService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    // Services
    private final CreateWalletService createWalletService;
    private final WalletBalanceService walletBalanceService;
    private final HistoricalBalanceService historicalBalanceService;
    private final DepositService depositService;
    private final WithdrawService withdrawService;
    private final TransferService transferService;

    // Mappers
    private final WalletMapper walletMapper;
    private final WithdrawMapper withdrawMapper;

    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@RequestBody @Valid @NotNull CreateWalletRequest request) {
        Wallet wallet = createWalletService.execute(request.userId());

        return ResponseEntity.ok(walletMapper.fromWalletToResponse(wallet));
    }

    @GetMapping("/{walletId}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable long walletId) {
        return ResponseEntity.ok(walletBalanceService.execute(walletId));
    }

    @PostMapping("/{targetWalletId}/deposit")
    public ResponseEntity<Void> deposit(@PathVariable @Positive long targetWalletId,
            @RequestBody @Valid @NotNull DepositRequest depositRequest) {
        depositService.execute(targetWalletId, depositRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{walletId}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable @Positive long walletId,
            @RequestBody @Valid @NotNull WithdrawRequest request) {
        withdrawService.execute(withdrawMapper.toWithdrawBean(walletId, request));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{sourceWalletId}/transfer")
    public ResponseEntity<Void> transfer(
            @PathVariable @Positive long sourceWalletId,
            @RequestBody @Valid @NotNull TransferRequest request) {
        transferService.execute(sourceWalletId, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{walletId}/balance-at")
    public ResponseEntity<BigDecimal> getBalanceAt(@PathVariable @Positive long walletId,
            @RequestParam("timestamp") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @NotNull LocalDateTime timestamp) {
        var balance = historicalBalanceService.getHistoricalBalance(walletId, timestamp);

        return ResponseEntity.ok(balance);
    }
}
