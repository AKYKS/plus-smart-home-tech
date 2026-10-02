package ru.practicum.analyzer.service.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class ScenarioRemovedHandler implements HubEventHandler {
    private final ScenarioRepository scenarioRepository;

    @Override
    public Class<?> getPayloadClass() {
        return ScenarioRemovedEventAvro.class;
    }

    @Override
    public void handle(HubEventAvro event) {
        ScenarioRemovedEventAvro scenarioRemovedEvent = (ScenarioRemovedEventAvro) event.getPayload();
        removeScenario(event.getHubId(), scenarioRemovedEvent.getName());
    }

    private void removeScenario(String hubId, String name) {
        scenarioRepository.findByHubIdAndName(hubId, name)
                .ifPresentOrElse(
                        (scenario) -> {
                            scenarioRepository.delete(scenario);
                            log.trace("scenario removed. hubId: '{}', name: '{}'", hubId, name);
                        },
                        () -> log.trace("scenario not exists. hubId: '{}', name: '{}'", hubId, name)
                );
    }
}
