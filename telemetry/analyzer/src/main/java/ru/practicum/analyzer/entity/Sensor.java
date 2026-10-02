package ru.practicum.analyzer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "sensors")
public class Sensor {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "hub_id")
    private String hubId;
}
