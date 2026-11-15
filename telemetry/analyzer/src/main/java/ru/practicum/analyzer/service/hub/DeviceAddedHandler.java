package ru.practicum.analyzer.service.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.analyzer.entity.Sensor;
import ru.practicum.analyzer.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class DeviceAddedHandler implements HubEventHandler {
    private final SensorRepository sensorRepository;

    @Override
    public Class<?> getPayloadClass() {
        return DeviceAddedEventAvro.class;
    }

    @Override
    public void handle(HubEventAvro event) {
        DeviceAddedEventAvro deviceAddedEvent = (DeviceAddedEventAvro) event.getPayload();
        String id = deviceAddedEvent.getId();
        String hubId = event.getHubId();
        sensorRepository.findByIdAndHubId(id, hubId)
                .ifPresentOrElse(
                        sensor -> log.trace("sensor already exist. id: '{}', hubId: '{}'", id, hubId),
                        () -> saveSensor(id, hubId)
                );
    }

    private void saveSensor(String id, String hubId) {
        Sensor sensor = Sensor.builder()
                .id(id)
                .hubId(hubId)
                .build();
        sensorRepository.save(sensor);
        log.trace("sensor saved: '{}'", sensor);
    }
}
