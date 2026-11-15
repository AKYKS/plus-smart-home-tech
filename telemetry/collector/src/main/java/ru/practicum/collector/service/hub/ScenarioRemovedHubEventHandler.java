package ru.practicum.collector.service.hub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
@SuppressWarnings("unused")
public class ScenarioRemovedHubEventHandler extends HubEventHandlerBase<ScenarioRemovedEventAvro> {

    public ScenarioRemovedHubEventHandler(
            @Value("${kafka.topics.hub-events}") String topic,
            KafkaEventProducer producer
    ) {
        super(topic, producer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    protected ScenarioRemovedEventAvro getPayload(HubEventProto event) {
        ScenarioRemovedEventProto scenarioRemovedHubEvent = event.getScenarioRemoved();
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedHubEvent.getId())
                .build();
    }
}
