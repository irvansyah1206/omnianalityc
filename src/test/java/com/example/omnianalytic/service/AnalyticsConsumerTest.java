package com.example.omnianalytic.service;

import com.example.omnianalytic.model.TransactionEvent;
import com.example.omnianalytic.model.document.TransactionDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsConsumerTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private ElasticsearchOperations elasticsearchOperations;

    @InjectMocks
    private AnalyticsConsumer analyticsConsumer;

    @Test
    void consume_ShouldUpdateRedisAndIndexToElastic() {
        // Arrange
        TransactionEvent event = new TransactionEvent(
                "tx-123", "card-999", new BigDecimal("500.00"), System.currentTimeMillis()
        );

        // Mock Redis behavior
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Act
        analyticsConsumer.consume(event);

        // Assert
        // 1. Verify Redis increment is called
        verify(valueOperations).increment(eq("total_spend:card-999"), eq(500.00));

        // 2. Verify Elastic save is called
        verify(elasticsearchOperations).save(any(TransactionDocument.class));
    }
}
