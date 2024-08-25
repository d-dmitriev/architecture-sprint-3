package ru.yandex.practicum.smarthome.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "temperature_sensors")
@Data
public class TemperatureSensor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private double currentTemperature;
    
    @Column(nullable = false)
    private LocalDateTime lastUpdated;
}
