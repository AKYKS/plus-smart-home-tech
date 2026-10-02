package ru.practicum.collector.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.serialization.AvroSerializer;

import java.time.Duration;
import java.time.Instant;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
@Slf4j
public class KafkaEventProducer implements AutoCloseable {
    private static final Duration CLOSE_DURATION = Duration.ofSeconds(10);

    private final KafkaProducer<String, SpecificRecordBase> producer;

    public KafkaEventProducer(@Value("${kafka.bootstrap-servers}") String bootstrapServers) {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializer.class);
        producer = new KafkaProducer<>(config);
    }

    @Override
    public void close() {
        producer.flush();
        producer.close(CLOSE_DURATION);
    }

    public CompletableFuture<RecordMetadata> send(
            String topic,
            Instant timestamp,
            String key,
            SpecificRecordBase value
    ) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                topic,
                null,
                timestamp.toEpochMilli(),
                key,
                value
        );

        CompletableFuture<RecordMetadata> future = new CompletableFuture<>();

        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                future.completeExceptionally(exception);
                log.error("failed to send record to topic={}, key={}: {}",
                        topic, key, exception.getMessage(), exception);
            } else {
                log.debug("record saved: topic={}, partition={}, offset={}, key={}",
                        metadata.topic(), metadata.partition(), metadata.offset(), key);
                future.complete(metadata);
            }
        });

        return future;
    }
}

