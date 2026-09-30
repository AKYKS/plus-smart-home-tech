package ru.practicum.collector.dto.hub.scenario.added;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "name must not be null")
    @NotEmpty(message = "name must not be empty")
    private String name;

    @NotNull(message = "conditions must not be null")
    @NotEmpty(message = "conditions must not be empty")
    @Valid
    private List<ScenarioCondition> conditions;

    @NotNull(message = "actions must not be null")
    @NotEmpty(message = "actions must not be empty")
    @Valid
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}