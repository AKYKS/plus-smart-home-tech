package ru.practicum.aggregator.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@AllArgsConstructor
@ConfigurationProperties("kafka.consumer")
@SuppressWarnings("unused")
public class ConsumerConfiguration {
    @Getter
    private List<String> topics;
    private Properties properties;
    private long attemptTimeoutMillis;

    @Bean
    public KafkaConsumer<String, SpecificRecordBase> kafkaConsumer() {
        return new KafkaConsumer<>(properties);
    }

    public Duration getAttemptTimeout() {
        return Duration.ofMillis(attemptTimeoutMillis);
    }
}
