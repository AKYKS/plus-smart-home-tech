package ru.practicum.collector.dto.hub.scenario.added.device.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class DeviceAction {
    private String sensorId;
    private ActionType type;
    private Integer value;
}
