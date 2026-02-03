package com.example.cryptowallet.dto;

import java.math.BigDecimal;

public record DepositRequest(String address, BigDecimal amount) {
}
