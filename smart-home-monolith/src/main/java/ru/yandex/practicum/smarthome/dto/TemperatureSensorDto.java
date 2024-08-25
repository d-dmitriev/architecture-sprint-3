package ru.yandex.practicum.smarthome.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TemperatureSensorDto {
    private Long id;
    private double currentTemperature;
    private LocalDateTime lastUpdated;
}
