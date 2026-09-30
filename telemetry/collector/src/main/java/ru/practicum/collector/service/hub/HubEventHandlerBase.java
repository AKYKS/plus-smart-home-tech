package ru.practicum.collector.service.hub;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.collector.dto.hub.HubEvent;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@RequiredArgsConstructor
public abstract class HubEventHandlerBase<P extends SpecificRecordBase> implements HubEventHandler {
    private static final String TOPIC = "telemetry.hubs.v1";

    private final KafkaEventProducer producer;

    protected abstract P getPayload(HubEvent event);

    @Override
    public void handle(HubEvent event) {
        String hubId = event.getHubId();
        Instant timestamp = event.getTimestamp();
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(hubId)
                .setTimestamp(timestamp)
                .setPayload(getPayload(event))
                .build();
        producer.send(TOPIC, timestamp, hubId, eventAvro);
    }
}
