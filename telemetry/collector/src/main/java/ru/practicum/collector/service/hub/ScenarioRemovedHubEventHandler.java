package ru.practicum.collector.service.hub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.collector.dto.hub.HubEvent;
import ru.practicum.collector.dto.hub.HubTypeNames;
import ru.practicum.collector.dto.hub.scenario.ScenarioRemovedHubEvent;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component(value = HubTypeNames.SCENARIO_REMOVED_EVENT)
@SuppressWarnings("unused")
public class ScenarioRemovedHubEventHandler extends HubEventHandlerBase<ScenarioRemovedEventAvro> {

    public ScenarioRemovedHubEventHandler(
            @Value("${kafka.topics.hub-events}") String topic,
            KafkaEventProducer producer
    ) {
        super(topic, producer);
    }

    @Override
    protected ScenarioRemovedEventAvro getPayload(HubEvent event) {
        ScenarioRemovedHubEvent scenarioRemovedHubEvent = (ScenarioRemovedHubEvent) event;
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedHubEvent.getName())
                .build();
    }
}
