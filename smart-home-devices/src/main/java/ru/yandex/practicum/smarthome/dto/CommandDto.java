package ru.yandex.practicum.smarthome.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CommandDto {
    private UUID deviceId;
    @NotNull(message = "value is required")
    private String type;
    private String value;
}
