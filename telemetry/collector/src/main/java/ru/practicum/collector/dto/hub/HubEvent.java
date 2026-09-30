package ru.practicum.collector.dto.hub;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.collector.dto.hub.device.DeviceRemovedHubEvent;
import ru.practicum.collector.dto.hub.device.added.DeviceAddedHubEvent;
import ru.practicum.collector.dto.hub.scenario.ScenarioRemovedHubEvent;
import ru.practicum.collector.dto.hub.scenario.added.ScenarioAddedHubEvent;

import java.time.Instant;

import static ru.practicum.collector.dto.hub.HubTypeNames.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = HubEventType.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceAddedHubEvent.class, name = DEVICE_ADDED_EVENT),
        @JsonSubTypes.Type(value = DeviceRemovedHubEvent.class, name = DEVICE_REMOVED_EVENT),
        @JsonSubTypes.Type(value = ScenarioAddedHubEvent.class, name = SCENARIO_ADDED_EVENT),
        @JsonSubTypes.Type(value = ScenarioRemovedHubEvent.class, name = SCENARIO_REMOVED_EVENT)
})
@Getter
@Setter
@ToString
public abstract class HubEvent {
    @NotBlank
    private String hubId;
    private final Instant timestamp = Instant.now();

    @NotNull
    public abstract HubEventType getType();
}
