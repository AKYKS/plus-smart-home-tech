package ru.practicum.deserialization;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

@SuppressWarnings("unused")
public class SensorsSnapshotDeserializer extends BaseAvroDeserializer<SensorsSnapshotAvro> {
    public SensorsSnapshotDeserializer() {
        super(SensorsSnapshotAvro.getClassSchema());
    }
}
