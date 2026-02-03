package com.example.cryptowallet.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponse(UUID id, String address, String currency, BigDecimal balance) {
}
