package com.example.cryptowallet.dto;

import java.math.BigDecimal;

public record TransferRequest(
        String fromAddress,
        String toAddress,
        BigDecimal amount
) {}