package ru.yandex.practicum.smarthome.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.smarthome.dto.CommandDto;
import ru.yandex.practicum.smarthome.entity.HeatingSystem;
import ru.yandex.practicum.smarthome.repository.HeatingSystemRepository;

@Component
@RequiredArgsConstructor
public class DevicesKafkaListener {
    private static final Logger logger = LoggerFactory.getLogger(DevicesKafkaListener.class);
    private final HeatingSystemRepository heatingSystemRepository;

    @KafkaListener(topics = "devices")
    public void listen(CommandDto message) {
        logger.info("Received message at group smart-home-monolith: {} {}", message.getDeviceId(), message.getType());
        HeatingSystem existingHeatingSystem = heatingSystemRepository.findFirstByDeviceId(message.getDeviceId());
        switch (message.getType()) {
            case "ON" -> existingHeatingSystem.setOn(true);
            case "OFF" -> existingHeatingSystem.setOn(false);
        }
        heatingSystemRepository.save(existingHeatingSystem);
    }
}
