package com.service.wallet.api.dto.api.response;

import java.math.BigDecimal;

public record WalletResponse(long id, BigDecimal balance) {
    
}
