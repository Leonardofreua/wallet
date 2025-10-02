package com.service.assignment.wallet.dto.api.response;

import java.math.BigDecimal;

public record WalletResponse(long id, BigDecimal balance) {
    
}
