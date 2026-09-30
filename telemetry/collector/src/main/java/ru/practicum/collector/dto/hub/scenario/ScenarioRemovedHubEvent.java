package ru.practicum.collector.dto.hub.scenario;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.collector.dto.hub.HubEvent;
import ru.practicum.collector.dto.hub.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioRemovedHubEvent extends HubEvent {
    private String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
