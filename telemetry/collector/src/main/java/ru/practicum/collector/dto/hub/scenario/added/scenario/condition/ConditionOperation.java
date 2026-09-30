package ru.practicum.collector.dto.hub.scenario.added.scenario.condition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;

@Getter
@ToString
@RequiredArgsConstructor
@SuppressWarnings("unused")
public enum ConditionOperation {
    EQUALS(ConditionOperationAvro.EQUALS),
    GREATER_THAN(ConditionOperationAvro.GREATER_THAN),
    LOWER_THAN(ConditionOperationAvro.LOWER_THAN);

    private final ConditionOperationAvro avro;
}
