package ru.practicum.collector.service.hub;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public abstract class HubEventHandlerBase<P extends SpecificRecordBase> implements HubEventHandler {
    @Value("${kafka.topics.hub-events}")
    private final String topic;

    private final KafkaEventProducer producer;

    protected abstract P getPayload(HubEventProto event);

    @Override
    public void handle(HubEventProto event) {
        String hubId = event.getHubId();
        Timestamp timestamp = event.getTimestamp();
        Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(hubId)
                .setTimestamp(instant)
                .setPayload(getPayload(event))
                .build();

        producer.send(topic, instant, hubId, eventAvro)
                .exceptionally(ex -> {
                    log.warn("Failed to send hub event hubId={}", hubId, ex);
                    return null;
                });
    }
}
