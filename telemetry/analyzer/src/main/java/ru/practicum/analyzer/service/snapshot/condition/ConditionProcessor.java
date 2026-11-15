package ru.practicum.analyzer.service.snapshot.condition;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.analyzer.entity.Condition;
import ru.practicum.analyzer.service.snapshot.condition.handler.ConditionHandler;
import ru.practicum.analyzer.service.snapshot.value.ValueProcessor;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ConditionProcessor {
    private final Map<String, ConditionHandler> conditionHandlerMap;
    private final ValueProcessor valueProcessor;

    public boolean isNotMatch(Condition condition, SensorStateAvro state) {
        ConditionHandler handler = conditionHandlerMap.get(condition.getOperation());
        if (handler == null) {
            return true;
        }
        int sensorValue = valueProcessor.process(state.getData(), condition.getType());
        int conditionValue = condition.getValue() != null ? condition.getValue() : 0;
        return !handler.handle(sensorValue, conditionValue);
    }
}