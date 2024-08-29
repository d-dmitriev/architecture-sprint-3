package ru.yandex.practicum.smarthome.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.smarthome.dto.TelemetryDto;
import ru.yandex.practicum.smarthome.entity.Telemetry;
import ru.yandex.practicum.smarthome.repository.TelemetryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TelemetryServiceImpl implements TelemetryService {
    private final TelemetryRepository telemetryRepository;
    @Autowired
    private KafkaTemplate<String, TelemetryDto> kafkaTemplate;

    @Override
    public void insertTelemetry(TelemetryDto temperatureSensorDto) {
        telemetryRepository.save(convertFromDto(temperatureSensorDto));
    }

    @Override
    public List<TelemetryDto> getTelemetry(UUID id) {
        Telemetry find = new Telemetry();
        find.setDeviceId(id);
        List<Telemetry> telemetryList = telemetryRepository.findAllByDeviceId(id);

        return telemetryList.stream().map(t -> convertToDto(t)).toList();
    }

    @Override
    public TelemetryDto getCurrentTelemetry(UUID id) {
        Telemetry find = new Telemetry();
        find.setDeviceId(id);
        Telemetry lastTelemetry = Optional.ofNullable(telemetryRepository.findFirstByDeviceIdOrderByLastUpdatedDesc(id))
                .orElseThrow(() -> new RuntimeException("DeviceId not found"));;

        return convertToDto(lastTelemetry);
    }

    private TelemetryDto convertToDto(Telemetry temperatureSensor) {
        TelemetryDto dto = new TelemetryDto();
        dto.setId(temperatureSensor.getId());
        dto.setDeviceId(temperatureSensor.getDeviceId());
        dto.setCurrentTemperature(temperatureSensor.getCurrentValueDouble());
        dto.setLastUpdated(temperatureSensor.getLastUpdated());
        return dto;
    }

    private Telemetry convertFromDto(TelemetryDto temperatureSensorDto) {
        Telemetry temperatureSensor = new Telemetry();
        temperatureSensor.setId(temperatureSensorDto.getId());
        temperatureSensor.setDeviceId(temperatureSensorDto.getDeviceId());
        temperatureSensor.setCurrentValueDouble(temperatureSensorDto.getCurrentTemperature());
        temperatureSensor.setLastUpdated(temperatureSensorDto.getLastUpdated());
        return temperatureSensor;
    }
}
