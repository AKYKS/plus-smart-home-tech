package ru.practicum.analyzer.service.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.analyzer.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class DeviceRemovedHandler implements HubEventHandler {
    private final SensorRepository sensorRepository;

    @Override
    public Class<?> getPayloadClass() {
        return DeviceRemovedEventAvro.class;
    }

    @Override
    public void handle(HubEventAvro event) {
        DeviceRemovedEventAvro deviceRemovedEvent = (DeviceRemovedEventAvro) event.getPayload();
        removeScenario(deviceRemovedEvent.getId());
    }

    private void removeScenario(String id) {
        sensorRepository.deleteById(id);
        log.trace("sensor removed. id: {}", id);
    }
}
