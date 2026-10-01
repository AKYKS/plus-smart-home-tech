package ru.practicum.collector.dto.hub.scenario.added.scenario.condition;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ScenarioCondition {
    @NotNull
    private String sensorId;
    @NotNull
    private ConditionType type;
    @NotNull
    private ConditionOperation operation;
    @NotNull(message = "value is required")
    private Object value;
}
