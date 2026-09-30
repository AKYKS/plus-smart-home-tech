package ru.practicum.collector.service.hub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.collector.dto.hub.HubEvent;
import ru.practicum.collector.dto.hub.HubTypeNames;
import ru.practicum.collector.dto.hub.scenario.added.ScenarioAddedHubEvent;
import ru.practicum.collector.dto.hub.scenario.added.device.action.DeviceAction;
import ru.practicum.collector.dto.hub.scenario.added.scenario.condition.ScenarioCondition;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.List;

@Component(value = HubTypeNames.SCENARIO_ADDED_EVENT)
@SuppressWarnings("unused")
public class ScenarioAddedHubEventHandler extends HubEventHandlerBase<ScenarioAddedEventAvro> {
    @Autowired
    public ScenarioAddedHubEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioAddedEventAvro getPayload(HubEvent event) {
        ScenarioAddedHubEvent scenarioAddedHubEvent = (ScenarioAddedHubEvent) event;
        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedHubEvent.getName())
                .setConditions(mapConditions(scenarioAddedHubEvent.getConditions()))
                .setActions(mapActions(scenarioAddedHubEvent.getActions()))
                .build();
    }

    private List<ScenarioConditionAvro> mapConditions(List<ScenarioCondition> conditions) {
        return conditions.stream()
                .map(condition ->
                        ScenarioConditionAvro.newBuilder()
                                .setSensorId(condition.getSensorId())
                                .setType(condition.getType().getAvro())
                                .setOperation(condition.getOperation().getAvro())
                                .setValue(condition.getValue())
                                .build()
                )
                .toList();
    }

    private List<DeviceActionAvro> mapActions(List<DeviceAction> actions) {
        return actions.stream()
                .map(action ->
                        DeviceActionAvro.newBuilder()
                                .setSensorId(action.getSensorId())
                                .setType(action.getType().getAvro())
                                .setValue(action.getValue())
                                .build()
                )
                .toList();
    }
}
