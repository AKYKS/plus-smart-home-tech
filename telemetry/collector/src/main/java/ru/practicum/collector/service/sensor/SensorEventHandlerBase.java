package ru.practicum.collector.service.sensor;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.collector.dto.sensor.SensorEvent;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@RequiredArgsConstructor
public abstract class SensorEventHandlerBase<P extends SpecificRecordBase> implements SensorEventHandler {
    private static final String TOPIC = "telemetry.sensors.v1";

    private final KafkaEventProducer producer;

    protected abstract P getPayload(SensorEvent event);

    @Override
    public void handle(SensorEvent event) {
        String id = event.getId();
        Instant timestamp = event.getTimestamp();
        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(id)
                .setHubId(event.getHubId())
                .setTimestamp(timestamp)
                .setPayload(getPayload(event))
                .build();
        producer.send(TOPIC, timestamp, id, eventAvro);
    }
}
