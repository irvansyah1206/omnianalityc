package com.example.omnianalytic.service;

import com.example.omnianalytic.model.TransactionEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class StreamProcessingService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Bean
    public KStream<String, TransactionEvent> processStream(StreamsBuilder builder) {
        KStream<String, TransactionEvent> stream = builder.stream("tx-events",
                Consumed.with(Serdes.String(), new JsonSerde<>(TransactionEvent.class)));

        stream
                // REDIS STRATEGY: Cek apakah User sedang di-blacklist (Cache Aside)
                .filter((key, value) -> !Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + key)))

                // KAFKA STREAMS: Windowing (Hitung transaksi per menit)
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(1)))
                .count()
                .toStream()

                // LOGIC FRAUD: Jika > 5 transaksi dalam 1 menit
                .filter((key, count) -> count > 5)
                .mapValues(count -> "POTENTIAL FRAUD DETECTED: " + count + " transactions")
                .to("fraud-alerts");

        return stream;
    }
}
