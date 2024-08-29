package ru.yandex.practicum.smarthome.component;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.smarthome.dto.TelemetryDto;
import ru.yandex.practicum.smarthome.service.TelemetryService;

@Component
@RequiredArgsConstructor
public class TelemetryKafkaListener {
    private final TelemetryService telemetryService;
    private static final Logger logger = LoggerFactory.getLogger(TelemetryKafkaListener.class);

    @KafkaListener(topics = "telemetry")
    public void listen(TelemetryDto message) {
        logger.info("Received message at group smart-home-telemetry: {}", message.getCurrentTemperature());
        telemetryService.insertTelemetry(message);
    }
}
