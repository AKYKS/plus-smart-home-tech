package ru.practicum.collector.dto.hub.scenario.added.scenario.condition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;

@Getter
@ToString
@RequiredArgsConstructor
@SuppressWarnings("unused")
public enum ConditionType {
    MOTION(ConditionTypeAvro.MOTION),
    LUMINOSITY(ConditionTypeAvro.LUMINOSITY),
    SWITCH(ConditionTypeAvro.SWITCH),
    TEMPERATURE(ConditionTypeAvro.TEMPERATURE),
    CO2LEVEL(ConditionTypeAvro.CO2LEVEL),
    HUMIDITY(ConditionTypeAvro.HUMIDITY);

    private final ConditionTypeAvro avro;
}