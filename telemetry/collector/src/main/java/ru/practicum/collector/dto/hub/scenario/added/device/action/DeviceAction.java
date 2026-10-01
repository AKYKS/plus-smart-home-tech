package ru.practicum.collector.dto.hub.scenario.added.device.action;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class DeviceAction {
    @NotNull(message = "sensorId is required")
    private String sensorId;
    @NotNull(message = "type is required")
    private ActionType type;
    @PositiveOrZero(message = "value must be >= 0")
    private Integer value;
}
