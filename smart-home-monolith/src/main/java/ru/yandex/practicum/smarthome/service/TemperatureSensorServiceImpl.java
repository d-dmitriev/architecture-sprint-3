package ru.yandex.practicum.smarthome.service;

import lombok.RequiredArgsConstructor;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.Producer;
//import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
//import ru.yandex.practicum.smarthome.dto.TelemetryDto;
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
//        Properties props = new Properties();
//        props.put("bootstrap.servers", "kafka.kafka:9092");
//        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//
//        Producer<String, String> producer = new KafkaProducer<>(props);
//        ProducerRecord<String, String> record = new ProducerRecord<>("telemetry", "{\"temperature\":"+temperature+"}");
//        producer.send(record);
//        producer.close();
//        TelemetryDto t = new TelemetryDto();
//        t.setTemperature(temperature);
//        kafkaTemplate.send("telemetry", t);

//        TemperatureSensor temperatureSensor = temperatureSensorRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("TemperatureSensor not found"));
//        temperatureSensor.setCurrentTemperature(temperature);
//        temperatureSensorRepository.save(temperatureSensor);
        TemperatureSensorDto t = new TemperatureSensorDto();
//        t.setId(id);
        t.setCurrentTemperature(temperature);
        t.setLastUpdated(LocalDateTime.now());
        kafkaTemplate.send("telemetry", t);
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
