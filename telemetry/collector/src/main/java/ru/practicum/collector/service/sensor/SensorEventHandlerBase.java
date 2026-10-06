package ru.practicum.collector.service.sensor;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public abstract class SensorEventHandlerBase<P extends SpecificRecordBase> implements SensorEventHandler {
    @Value("${kafka.topics.sensor-events}")
    private final String topic;

    private final KafkaEventProducer producer;

    protected abstract P getPayload(SensorEventProto event);

    @Override
    public void handle(SensorEventProto event) {
        String id = event.getId();
        String hubId = event.getHubId();
        Timestamp timestamp = event.getTimestamp();
        Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(id)
                .setHubId(hubId)
                .setTimestamp(instant)
                .setPayload(getPayload(event))
                .build();
        producer.send(topic, instant, hubId, eventAvro)
                .exceptionally(ex -> {
                    log.warn("Failed to send sensor event id={}, hubId={}", id, hubId, ex);
                    return null;
                });
    }
}
