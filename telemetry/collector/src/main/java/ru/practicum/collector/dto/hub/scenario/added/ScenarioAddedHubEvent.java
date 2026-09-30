package ru.practicum.collector.dto.hub.scenario.added;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.collector.dto.hub.HubEvent;
import ru.practicum.collector.dto.hub.HubEventType;
import ru.practicum.collector.dto.hub.scenario.added.device.action.DeviceAction;
import ru.practicum.collector.dto.hub.scenario.added.scenario.condition.ScenarioCondition;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioAddedHubEvent extends HubEvent {
    private String name;
    private List<ScenarioCondition> conditions;
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}