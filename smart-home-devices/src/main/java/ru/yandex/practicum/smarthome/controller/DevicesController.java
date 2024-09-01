package ru.yandex.practicum.smarthome.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.smarthome.dto.DeviceDto;
import ru.yandex.practicum.smarthome.service.DevicesService;
// import ru.yandex.practicum.smarthome.service.TelemetryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DevicesController {
    private final DevicesService devicesService;
    private static final Logger logger = LoggerFactory.getLogger(DevicesController.class);

    @PostMapping
    public ResponseEntity<DeviceDto> register(@RequestHeader HttpHeaders requestHeaders, @RequestBody DeviceDto device) {
        logger.info("Register device with name {}", device.getName());
        return ResponseEntity.ok().headers(requestHeaders).body(devicesService.register(device));
    }

    @GetMapping("/{device_id}")
    public ResponseEntity<DeviceDto> getDevice(@PathVariable("device_id") UUID deviceId, @RequestHeader HttpHeaders requestHeaders) {
        logger.info("Fetching info for device with id {}", deviceId);
        return ResponseEntity.ok().headers(requestHeaders).body(devicesService.getDeviceInfo(deviceId));
    }

    @PutMapping("/{device_id}/status")
    public ResponseEntity<DeviceDto> updateDeviceStatus(@PathVariable("device_id") UUID deviceId, @RequestHeader HttpHeaders requestHeaders, @RequestParam String status) {
        logger.info("Updating status for device with id {}", deviceId);
        return ResponseEntity.noContent().headers(requestHeaders).build();
    }

    @PostMapping("/{device_id}/commands")
    public ResponseEntity<DeviceDto> sendDeviceCommand(@PathVariable("device_id") UUID deviceId, @RequestHeader HttpHeaders requestHeaders, @RequestParam String command) {
        logger.info("Sending command for device with id {}", deviceId);
        return ResponseEntity.noContent().headers(requestHeaders).build();
    }
}
