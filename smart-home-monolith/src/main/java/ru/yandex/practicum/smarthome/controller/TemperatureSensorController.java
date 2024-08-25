package ru.yandex.practicum.smarthome.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.smarthome.dto.TemperatureSensorDto;
import ru.yandex.practicum.smarthome.service.TemperatureSensorService;

@RestController
@RequestMapping("/api/temperature")
@RequiredArgsConstructor
public class TemperatureSensorController {
    private final TemperatureSensorService temperatureSensorService;
    private static final Logger logger = LoggerFactory.getLogger(TemperatureSensorController.class);

    @PostMapping
    public ResponseEntity<TemperatureSensorDto> initHeatingSystem(@RequestBody TemperatureSensorDto temperatureSensorDto) {
        logger.info("Init temperature sensor with id {}", temperatureSensorDto.getId());
        return ResponseEntity.ok(temperatureSensorService.initTemperatureSensor(temperatureSensorDto));
    }

    @GetMapping("/{id}/current-temperature")
    public ResponseEntity<Double> getCurrentTemperature(@PathVariable("id") Long id, @RequestHeader HttpHeaders requestHeaders) {
        logger.info("Fetching current temperature for temperature sensor with id {}", id);
        return ResponseEntity.ok().headers(requestHeaders).body(temperatureSensorService.getCurrentTemperature(id));
//        return new ResponseEntity<>(temperatureSensorService.getCurrentTemperature(id), requestHeaders, HttpStatus.OK);// ResponseEntity.ok(temperatureSensorService.getCurrentTemperature(id));
    }

    @PostMapping("/{id}/set-temperature")
    public ResponseEntity<Void> setTargetTemperature(@PathVariable("id") Long id, @RequestParam double temperature, @RequestHeader HttpHeaders requestHeaders) {
        logger.info("Setting target temperature to {} for temperature sensor with id {}", temperature, id);
        temperatureSensorService.setCurrentTemperature(id, temperature);
        // TODO: Implement automatic temperature maintenance logic in the service layer
//        return new ResponseEntity<>(temperatureSensorService.getCurrentTemperature(id), requestHeaders, HttpStatus.NO_CONTENT)
        return ResponseEntity.noContent().headers(requestHeaders).build();
    }
}
