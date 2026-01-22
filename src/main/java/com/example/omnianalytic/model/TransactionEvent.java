package com.example.omnianalytic.model;

import java.math.BigDecimal;

public record TransactionEvent(
        String transactionId,
        String cardId,
        BigDecimal amount,
        long timestamp
) {}
