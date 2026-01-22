package com.example.omnianalytic.service;

import com.example.omnianalytic.model.dto.TransactionRequest;
import com.example.omnianalytic.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IngestionServiceTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private IngestionService ingestionService;

    @Test
    void processBulkRequest_ShouldFilterAndProcessValidTransactions() {
        // Arrange
        // Sesuai record: merchant, amount, category, cardId
        TransactionRequest validReq = new TransactionRequest(
                "Merchant A", 
                new BigDecimal("100.00"), 
                "Electronics", 
                "card-123"
        );
        
        TransactionRequest invalidReq = new TransactionRequest(
                "Merchant B", 
                new BigDecimal("-50.00"), 
                "Groceries", 
                "card-456"
        );

        List<TransactionRequest> requests = List.of(validReq, invalidReq);

        // Act
        ingestionService.processBulkRequest(requests);

        // Assert
        // 1. Verify only 1 transaction is saved to DB (karena yang satu invalid amount < 0)
        verify(repository, times(1)).saveAll(anyList());

        // 2. Verify Kafka sends event for the valid transaction
        // Topic "tx-events" sesuai dengan yang ada di IngestionService
        verify(kafkaTemplate, times(1)).send(eq("tx-events"), any(), any());
    }
}
