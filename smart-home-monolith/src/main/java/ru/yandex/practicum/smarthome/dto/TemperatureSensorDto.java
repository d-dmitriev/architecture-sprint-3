package ru.yandex.practicum.smarthome.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TemperatureSensorDto {
    private Long id;
    private UUID deviceId;
    private double currentTemperature;
    private LocalDateTime lastUpdated;
}
