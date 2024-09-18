package ru.yandex.practicum.smarthome.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "telemetry")
@Data
public class Telemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID deviceId;

    @Column
    private double currentValueDouble;

    @Column
    private int currentValueInteger;

    @Column
    private String currentValueString;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;
}
