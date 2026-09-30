package ru.practicum.collector.dto.hub.device.added;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

@Getter
@ToString
@RequiredArgsConstructor
@SuppressWarnings("unused")
public enum DeviceType {
    MOTION_SENSOR(DeviceTypeAvro.MOTION_SENSOR),
    TEMPERATURE_SENSOR(DeviceTypeAvro.TEMPERATURE_SENSOR),
    LIGHT_SENSOR(DeviceTypeAvro.LIGHT_SENSOR),
    CLIMATE_SENSOR(DeviceTypeAvro.CLIMATE_SENSOR),
    SWITCH_SENSOR(DeviceTypeAvro.SWITCH_SENSOR);

    private final DeviceTypeAvro avro;
}
