package ru.yandex.practicum.smarthome.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.smarthome.dto.TemperatureSensorDto;

@Component
@RequiredArgsConstructor
public class TelemetryKafkaListener {
    private final TemperatureSensorService temperatureSensorService;

    @KafkaListener(topics = "telemetry", concurrency = "2")
    public void listen(TemperatureSensorDto message) {
        System.out.println("!!! Received message at group smart-home-monolith: " + message.getCurrentTemperature());
//        temperatureSensorService.initTemperatureSensor(message);
    }
}
