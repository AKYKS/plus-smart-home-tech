package ru.practicum.collector.dto.hub.scenario.added.device.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;

@Getter
@ToString
@RequiredArgsConstructor
@SuppressWarnings("unused")
public enum ActionType {
    ACTIVATE(ActionTypeAvro.ACTIVATE),
    DEACTIVATE(ActionTypeAvro.DEACTIVATE),
    INVERSE(ActionTypeAvro.INVERSE),
    SET_VALUE(ActionTypeAvro.SET_VALUE);

    private final ActionTypeAvro avro;
}
