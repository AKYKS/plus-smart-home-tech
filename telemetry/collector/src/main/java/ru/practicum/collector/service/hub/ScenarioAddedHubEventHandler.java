package ru.practicum.collector.service.hub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

@Component
@SuppressWarnings("unused")
public class ScenarioAddedHubEventHandler extends HubEventHandlerBase<ScenarioAddedEventAvro> {

    public ScenarioAddedHubEventHandler(
            @Value("${kafka.topics.hub-events}") String topic,
            KafkaEventProducer producer
    ) {
        super(topic, producer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    protected ScenarioAddedEventAvro getPayload(HubEventProto event) {
        ScenarioAddedEventProto scenarioAddedHubEvent = event.getScenarioAdded();
        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedHubEvent.getId())
                .setConditions(mapConditions(scenarioAddedHubEvent.getConditionList()))
                .setActions(mapActions(scenarioAddedHubEvent.getActionList()))
                .build();
    }

    private List<ScenarioConditionAvro> mapConditions(List<ScenarioConditionProto> conditions) {
        return conditions.stream()
                .map(condition ->
                        ScenarioConditionAvro.newBuilder()
                                .setSensorId(condition.getSensorId())
                                .setType(mapConditionType(condition.getType()))
                                .setOperation(mapConditionOperation(condition.getOperation()))
                                .setValue(getValue(condition))
                                .build()
                )
                .toList();
    }

    private ConditionTypeAvro mapConditionType(ConditionTypeProto conditionTypeProto) {
        return switch (conditionTypeProto) {
            case MOTION -> ConditionTypeAvro.MOTION;
            case LUMINOSITY -> ConditionTypeAvro.LUMINOSITY;
            case SWITCH -> ConditionTypeAvro.SWITCH;
            case TEMPERATURE -> ConditionTypeAvro.TEMPERATURE;
            case CO2LEVEL -> ConditionTypeAvro.CO2LEVEL;
            case HUMIDITY -> ConditionTypeAvro.HUMIDITY;
            default -> null;
        };
    }

    private ConditionOperationAvro mapConditionOperation(ConditionOperationProto conditionOperationProto) {
        return switch (conditionOperationProto) {
            case EQUALS -> ConditionOperationAvro.EQUALS;
            case GREATER_THAN -> ConditionOperationAvro.GREATER_THAN;
            case LOWER_THAN -> ConditionOperationAvro.LOWER_THAN;
            default -> null;
        };
    }

    private Object getValue(ScenarioConditionProto conditionProto) {
        if (conditionProto.hasBoolValue()) {
            return conditionProto.getBoolValue();
        } else {
            return conditionProto.getIntValue();
        }
    }

    private List<DeviceActionAvro> mapActions(List<DeviceActionProto> actions) {
        return actions.stream()
                .map(action ->
                        DeviceActionAvro.newBuilder()
                                .setSensorId(action.getSensorId())
                                .setType(mapActionType(action.getType()))
                                .setValue(action.getValue())
                                .build()
                )
                .toList();
    }

    private ActionTypeAvro mapActionType(ActionTypeProto actionTypeProto) {
        return switch (actionTypeProto) {
            case ACTIVATE -> ActionTypeAvro.ACTIVATE;
            case DEACTIVATE -> ActionTypeAvro.DEACTIVATE;
            case INVERSE -> ActionTypeAvro.INVERSE;
            case SET_VALUE -> ActionTypeAvro.SET_VALUE;
            default -> null;
        };
    }
}

