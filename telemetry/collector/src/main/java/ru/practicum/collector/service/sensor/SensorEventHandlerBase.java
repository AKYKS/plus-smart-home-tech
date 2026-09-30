package ru.practicum.collector.service.sensor;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.collector.dto.sensor.SensorEvent;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@RequiredArgsConstructor
public abstract class SensorEventHandlerBase<P extends SpecificRecordBase> implements SensorEventHandler {
    @Value("${kafka.topics.sensor-events}")
    private String TOPIC;

    private final KafkaEventProducer producer;

    protected abstract P getPayload(SensorEvent event);

    @Override
    public void handle(SensorEvent event) {
        String hubId = event.getHubId();
        String id = event.getId();
        Instant timestamp = event.getTimestamp();
        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(id)
                .setHubId(hubId)
                .setTimestamp(timestamp)
                .setPayload(getPayload(event))
                .build();
        producer.send(TOPIC, timestamp, hubId, eventAvro);
    }
}
