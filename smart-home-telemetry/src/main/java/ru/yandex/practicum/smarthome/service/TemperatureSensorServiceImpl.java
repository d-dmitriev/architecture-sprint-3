package ru.yandex.practicum.smarthome.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.smarthome.dto.TemperatureSensorDto;
import ru.yandex.practicum.smarthome.entity.TemperatureSensor;
import ru.yandex.practicum.smarthome.repository.TemperatureSensorRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TemperatureSensorServiceImpl implements TemperatureSensorService {
    private final TemperatureSensorRepository temperatureSensorRepository;
    @Autowired
    private KafkaTemplate<String, TemperatureSensorDto> kafkaTemplate;

    @Override
    public TemperatureSensorDto initTemperatureSensor(TemperatureSensorDto temperatureSensorDto) {
        TemperatureSensor updatedTemperatureSensor =  temperatureSensorRepository.save(convertFromDto(temperatureSensorDto));
        return convertToDto(updatedTemperatureSensor);
    }

    @Override
    public void setCurrentTemperature(Long id, double temperature) {
        TemperatureSensor temperatureSensor = temperatureSensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TemperatureSensor not found"));
        temperatureSensor.setCurrentTemperature(temperature);
        temperatureSensorRepository.save(temperatureSensor);
    }

    @Override
    public Double getCurrentTemperature(Long id) {
        TemperatureSensor temperatureSensor = temperatureSensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TemperatureSensor not found"));
        return temperatureSensor.getCurrentTemperature();
    }

    private TemperatureSensorDto convertToDto(TemperatureSensor temperatureSensor) {
        TemperatureSensorDto dto = new TemperatureSensorDto();
        dto.setId(temperatureSensor.getId());
        dto.setCurrentTemperature(temperatureSensor.getCurrentTemperature());
        dto.setLastUpdated(temperatureSensor.getLastUpdated());
        return dto;
    }

    private TemperatureSensor convertFromDto(TemperatureSensorDto temperatureSensorDto) {
        TemperatureSensor temperatureSensor = new TemperatureSensor();
        temperatureSensor.setId(temperatureSensorDto.getId());
        temperatureSensor.setCurrentTemperature(temperatureSensorDto.getCurrentTemperature());
        temperatureSensor.setLastUpdated(temperatureSensorDto.getLastUpdated());
        return temperatureSensor;
    }
}
