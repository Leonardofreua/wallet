package com.service.wallet.api.type;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record Amount(
    
    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    BigDecimal value) {
    
}
