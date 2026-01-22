package com.example.omnianalytic.service;

import com.example.omnianalytic.model.TransactionEvent;
import com.example.omnianalytic.model.dto.TransactionRequest;
import com.example.omnianalytic.model.entity.TransactionEntity;
import com.example.omnianalytic.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngestionService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TransactionRepository repository;

    @Transactional
    public void processBulkRequest(List<TransactionRequest> requests) {
        // JAVA STREAM: Memfilter dan Transformasi Data
        List<TransactionEntity> validTransactions = requests.stream()
                .filter(req -> req.amount().compareTo(BigDecimal.ZERO) > 0)
                .map(req -> TransactionEntity.builder()
                        .id(UUID.randomUUID())
                        .merchant(req.merchant())
                        .amount(req.amount())
                        .category(req.category())
                        .createdAt(LocalDateTime.now())
                        .build())
                .toList();

        // 1. Simpan ke DB (Native SQL Query nantinya baca dari sini)
        repository.saveAll(validTransactions);

        // 2. Kirim ke Kafka untuk Analisa Fraud Real-time
        validTransactions.forEach(tx -> {
            TransactionEvent event = new TransactionEvent(
                    tx.getId().toString(),
                    "CARD-123", // Simulasi Card ID
                    tx.getAmount(),
                    System.currentTimeMillis()
            );
            kafkaTemplate.send("tx-events", event.cardId(), event);
        });
    }
}
