package ru.yandex.practicum.smarthome.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TelemetryDto {
    private UUID deviceId;
    private double currentTemperature;
    private LocalDateTime lastUpdated;
}
