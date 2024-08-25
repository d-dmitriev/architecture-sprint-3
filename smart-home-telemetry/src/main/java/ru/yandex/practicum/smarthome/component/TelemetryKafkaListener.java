package ru.yandex.practicum.smarthome.component;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.smarthome.dto.TemperatureSensorDto;
import ru.yandex.practicum.smarthome.service.TemperatureSensorService;

@Component
@RequiredArgsConstructor
public class TelemetryKafkaListener {
    private final TemperatureSensorService temperatureSensorService;

    @KafkaListener(topics = "telemetry")
    public void listen(TemperatureSensorDto message) {
        System.out.println("!!!@@!! Received message at group sample07-1: " + message.getCurrentTemperature());
        temperatureSensorService.initTemperatureSensor(message);
    }
}
