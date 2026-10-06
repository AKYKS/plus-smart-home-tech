package ru.practicum.aggregator.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AggregationService {
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro snapshot = snapshots.get(event.getHubId());
        if (snapshot == null) {
            snapshot = buildSnapshot(event);
        }
        if (isOutdated(snapshot, event)) {
            return Optional.empty();
        }
        SensorsSnapshotAvro snapshotUpdate = buildSnapshotUpdate(snapshot, event);
        snapshots.put(event.getHubId(), snapshotUpdate);
        return Optional.of(snapshotUpdate);
    }

    private SensorsSnapshotAvro buildSnapshot(SensorEventAvro event) {
        return SensorsSnapshotAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setSensorsState(new HashMap<>())
                .build();
    }

    private boolean isOutdated(SensorsSnapshotAvro snapshot, SensorEventAvro event) {
        if (snapshot.getSensorsState().get(event.getId()) != null) {
            SensorStateAvro oldState = snapshot.getSensorsState().get(event.getId());
            Object oldStateData = oldState.getData();
            return oldState.getTimestamp().isAfter(event.getTimestamp())
                    || (oldStateData != null && oldStateData.equals(event.getPayload()));
        }
        return false;
    }

    private SensorsSnapshotAvro buildSnapshotUpdate(SensorsSnapshotAvro snapshot, SensorEventAvro event) {
        return SensorsSnapshotAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setSensorsState(buildStateMapUpdate(snapshot, event))
                .build();
    }

    private Map<String, SensorStateAvro> buildStateMapUpdate(SensorsSnapshotAvro snapshot, SensorEventAvro event) {
        Map<String, SensorStateAvro> stateMapUpdate = new HashMap<>(snapshot.getSensorsState());
        stateMapUpdate.put(
                event.getId(),
                SensorStateAvro.newBuilder()
                        .setTimestamp(event.getTimestamp())
                        .setData(event.getPayload())
                        .build()
        );
        return stateMapUpdate;
    }
}

