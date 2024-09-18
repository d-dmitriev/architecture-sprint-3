package ru.yandex.practicum.smarthome.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CommandDto {
    private UUID deviceId;
    private String type;
    private String value;
}
