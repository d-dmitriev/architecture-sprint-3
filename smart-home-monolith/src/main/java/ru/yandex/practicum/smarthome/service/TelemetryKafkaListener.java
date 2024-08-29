package ru.yandex.practicum.smarthome.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.smarthome.dto.TelemetryDto;

@Component
@RequiredArgsConstructor
public class TelemetryKafkaListener {
    private static final Logger logger = LoggerFactory.getLogger(TelemetryKafkaListener.class);

    @KafkaListener(topics = "telemetry", concurrency = "2")
    public void listen(TelemetryDto message) {
        logger.info("Received message at group smart-home-monolith: {}", message.getCurrentTemperature());
    }
}
