package ru.yandex.practicum.smarthome.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DeviceDto {
    private UUID deviceId;
    private String name;
    private String status;
}