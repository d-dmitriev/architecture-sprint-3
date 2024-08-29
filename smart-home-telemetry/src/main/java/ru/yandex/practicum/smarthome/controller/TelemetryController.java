package ru.yandex.practicum.smarthome.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.smarthome.dto.TelemetryDto;
import ru.yandex.practicum.smarthome.service.TelemetryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class TelemetryController {
    private final TelemetryService telemetryService;
    private static final Logger logger = LoggerFactory.getLogger(TelemetryController.class);

    @GetMapping("/{id}/telemetry")
    public ResponseEntity<List<TelemetryDto>> getTelemetry(@PathVariable("id") UUID id, @RequestHeader HttpHeaders requestHeaders) {
        logger.info("Fetching current temperature for temperature sensor with id {}", id);
        return ResponseEntity.ok().headers(requestHeaders).body(telemetryService.getTelemetry(id));
    }

    @GetMapping("/{id}/telemetry/latest")
    public ResponseEntity<TelemetryDto> getLatestTelemetry(@PathVariable("id") UUID id, @RequestHeader HttpHeaders requestHeaders) {
        logger.info("Fetching current temperature for temperature sensor with id {}", id);
        return ResponseEntity.ok().headers(requestHeaders).body(telemetryService.getCurrentTelemetry(id));
    }
}
