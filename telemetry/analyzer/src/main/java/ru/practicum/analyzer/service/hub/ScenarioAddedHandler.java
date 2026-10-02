package ru.practicum.analyzer.service.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.analyzer.entity.*;
import ru.practicum.analyzer.repository.ActionRepository;
import ru.practicum.analyzer.repository.ConditionRepository;
import ru.practicum.analyzer.repository.ScenarioRepository;
import ru.practicum.analyzer.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class ScenarioAddedHandler implements HubEventHandler {
    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final SensorRepository sensorRepository;
    private final ActionRepository actionRepository;

    @Override
    public Class<?> getPayloadClass() {
        return ScenarioAddedEventAvro.class;
    }

    @Override
    public void handle(HubEventAvro event) {
        ScenarioAddedEventAvro scenarioAddedEvent = (ScenarioAddedEventAvro) event.getPayload();
        Scenario scenario = buildAndSaveScenario(event.getHubId(), scenarioAddedEvent.getName());
        saveConditions(scenario, scenarioAddedEvent.getConditions());
        saveActions(scenario, scenarioAddedEvent.getActions());
        saveScenario(scenario);
    }

    private Scenario buildAndSaveScenario(String hubId, String name) {
        return scenarioRepository.save(
                Scenario.builder()
                        .hubId(hubId)
                        .name(name)
                        .build()
        );
    }

    private void saveConditions(Scenario scenario, List<ScenarioConditionAvro> conditions) {
        for (ScenarioConditionAvro condition : conditions) {
            Condition savedCondition = saveCondition(condition);
            Sensor sensor = findSensor(condition.getSensorId());
            addScenarioCondition(scenario, sensor, savedCondition);
        }
    }

    private Condition saveCondition(ScenarioConditionAvro condition) {
        return conditionRepository.save(
                Condition.builder()
                        .type(condition.getType().name())
                        .operation(condition.getOperation().name())
                        .value(mapValue(condition.getValue()))
                        .build()
        );
    }

    private Integer mapValue(Object value) {
        return switch (value) {
            case null -> null;
            case Number number -> number.intValue();
            case Boolean isTrue -> isTrue ? 1 : 0;
            default -> throw new IllegalArgumentException("unexpected value. class: " + value.getClass());
        };
    }

    private Sensor findSensor(String id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("sensor not found. id: " + id));
    }

    private void addScenarioCondition(Scenario scenario, Sensor sensor, Condition condition) {
        scenario.getConditions().add(
                ScenarioCondition.builder()
                        .id(
                                ScenarioConditionId.builder()
                                        .scenarioId(scenario.getId())
                                        .sensorId(sensor.getId())
                                        .conditionId(condition.getId())
                                        .build()
                        )
                        .scenario(scenario)
                        .sensor(sensor)
                        .condition(condition)
                        .build()
        );
    }

    private void saveActions(Scenario scenario, List<DeviceActionAvro> actions) {
        for (DeviceActionAvro action : actions) {
            Action savedAction = saveAction(action);
            Sensor sensor = findSensor(action.getSensorId());
            addScenarioAction(scenario, sensor, savedAction);
        }
    }

    private Action saveAction(DeviceActionAvro action) {
        return actionRepository.save(
                Action.builder()
                        .type(action.getType().name())
                        .value(action.getValue())
                        .build()
        );
    }

    private void addScenarioAction(Scenario scenario, Sensor sensor, Action action) {
        scenario.getActions().add(
                ScenarioAction.builder()
                        .id(
                                ScenarioActionId.builder()
                                        .scenarioId(scenario.getId())
                                        .sensorId(sensor.getId())
                                        .actionId(action.getId())
                                        .build()
                        )
                        .scenario(scenario)
                        .sensor(sensor)
                        .action(action)
                        .build()
        );
    }

    private void saveScenario(Scenario scenario) {
        scenarioRepository.save(scenario);
        log.trace("scenario saved: {}", scenario);
    }
}
