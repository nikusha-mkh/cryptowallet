package com.example.cryptowallet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DepositRequest(@NotBlank String address,
                             @NotNull @Positive(message = "Amount must be greater than zero")
                             BigDecimal amount) {
}
