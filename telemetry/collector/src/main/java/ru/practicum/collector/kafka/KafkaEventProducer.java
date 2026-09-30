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

    public void send(String topic, Instant timestamp, String key, SpecificRecordBase value) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                topic,
                null,
                timestamp.toEpochMilli(),
                key,
                value
        );
        Future<RecordMetadata> futureResult = producer.send(record);
        flush(futureResult, record, topic);
    }

    private void flush(Future<RecordMetadata> futureResult, ProducerRecord<String, SpecificRecordBase> record, String topic) {
        producer.flush();
        try {
            RecordMetadata metadata = futureResult.get();
            log.info("record '{}' was successfully saved: topic '{}', partition '{}', offset '{}' ",
                    record, metadata.topic(), metadata.partition(), metadata.offset());
        } catch (InterruptedException | ExecutionException e) {
            log.warn("couldn't record '{}' in topic '{}'", record, topic, e);
        }
    }
}

