package com.example.cryptowallet.dto;

import java.util.UUID;

public record CreateWalletRequest(UUID userId,String currency) {
}
