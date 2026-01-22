package com.example.omnianalytic.service;

import com.example.omnianalytic.model.TransactionEvent;
import com.example.omnianalytic.model.document.TransactionDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsConsumer {

    private final StringRedisTemplate redisTemplate;
    private final ElasticsearchOperations elasticsearchOperations;

    @KafkaListener(topics = "tx-events", groupId = "analytics-group")
    public void consume(TransactionEvent event) {
        log.info("Received event: {}", event);

        // 1. REDIS: Real-time Aggregation (Total Spend per Card)
        // Increment total spending for this card
        String redisKey = "total_spend:" + event.cardId();
        // Note: increment supports double in newer Redis versions or via custom logic, 
        // but for simplicity here we treat it as double string or use increment(long) if amount was long.
        // Since amount is BigDecimal, we'll just set/get for simplicity or use incrementByFloat
        redisTemplate.opsForValue().increment(redisKey, event.amount().doubleValue());

        // 2. ELASTICSEARCH: Indexing for Search
        TransactionDocument doc = TransactionDocument.builder()
                .id(event.transactionId())
                .cardId(event.cardId())
                .amount(event.amount())
                .timestamp(event.timestamp())
                .build();
        
        elasticsearchOperations.save(doc);
        log.info("Indexed to Elastic & Updated Redis for Card: {}", event.cardId());
    }
}
