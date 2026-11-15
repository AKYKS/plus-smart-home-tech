package ru.practicum.deserialization;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@SuppressWarnings("unused")
public class SensorEventDeserializer extends BaseAvroDeserializer<SensorEventAvro> {
    public SensorEventDeserializer() {
        super(SensorEventAvro.getClassSchema());
    }
}
