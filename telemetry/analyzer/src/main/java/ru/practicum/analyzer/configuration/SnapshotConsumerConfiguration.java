package ru.practicum.analyzer.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Properties;

@AllArgsConstructor
@ConfigurationProperties("kafka.consumer.snapshot")
@SuppressWarnings("unused")
public class SnapshotConsumerConfiguration {
    @Getter
    private List<String> topics;
    private Properties properties;

    @Bean
    public KafkaConsumer<String, SpecificRecordBase> snapshotConsumer() {
        return new KafkaConsumer<>(properties);
    }
}
