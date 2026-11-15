package ru.practicum.aggregator.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

@AllArgsConstructor
@ConfigurationProperties("kafka.producer")
@SuppressWarnings("unused")
public class ProducerConfiguration {
    @Getter
    private String topic;
    private Properties properties;

    @Bean
    public KafkaProducer<String, SpecificRecordBase> kafkaProducer() {
        return new KafkaProducer<>(properties);
    }
}
