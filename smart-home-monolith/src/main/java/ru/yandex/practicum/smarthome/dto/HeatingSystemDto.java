package ru.yandex.practicum.smarthome.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class HeatingSystemDto {
    private Long id;
    private UUID deviceId;
    private boolean isOn;
    private double targetTemperature;
    private double currentTemperature;
}