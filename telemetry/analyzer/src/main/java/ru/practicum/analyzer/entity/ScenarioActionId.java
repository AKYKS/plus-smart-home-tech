package ru.practicum.analyzer.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class ScenarioActionId {
    private Long scenarioId;
    private String sensorId;
    private Long actionId;
}
