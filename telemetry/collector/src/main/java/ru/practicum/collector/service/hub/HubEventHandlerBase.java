package ru.practicum.collector.service.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.collector.dto.hub.HubEvent;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public abstract class HubEventHandlerBase<P extends SpecificRecordBase> implements HubEventHandler {
    @Value("${kafka.topics.hub-events}")
    private final String topic;

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

        producer.send(topic, timestamp, hubId, eventAvro)
                .exceptionally(ex -> {
                    log.warn("Failed to send hub event hubId={}", hubId, ex);
                    return null;
                });
    }
}
