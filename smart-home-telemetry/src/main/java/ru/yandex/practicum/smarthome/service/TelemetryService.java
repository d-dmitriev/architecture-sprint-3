package ru.yandex.practicum.smarthome.service;

import ru.yandex.practicum.smarthome.dto.TelemetryDto;

import java.util.List;
import java.util.UUID;

public interface TelemetryService {
    void insertTelemetry(TelemetryDto temperatureSensor);
    TelemetryDto getCurrentTelemetry(UUID deviceId);
    List<TelemetryDto> getTelemetry(UUID deviceId);
}
