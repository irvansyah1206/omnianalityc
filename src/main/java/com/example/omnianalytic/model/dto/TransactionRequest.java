package com.example.omnianalytic.model.dto;

import java.math.BigDecimal;
public record TransactionRequest(
        String merchant,
        BigDecimal amount,
        String category,
        String cardId // Untuk deteksi fraud
) {}
