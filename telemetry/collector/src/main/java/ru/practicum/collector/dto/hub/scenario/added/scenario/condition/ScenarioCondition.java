package ru.practicum.collector.dto.hub.scenario.added.scenario.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ScenarioCondition {
    private String sensorId;
    private ConditionType type;
    private ConditionOperation operation;
    private Object value;
}
