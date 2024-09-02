package ru.yandex.practicum.smarthome.service;

import jakarta.persistence.RollbackException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.smarthome.dto.CommandDto;
import ru.yandex.practicum.smarthome.dto.DeviceDto;
import ru.yandex.practicum.smarthome.entity.Device;
import ru.yandex.practicum.smarthome.repository.DevicesRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DevicesServiceImpl implements DevicesService {
    private final DevicesRepository devicesRepository;
    @Autowired
    private KafkaTemplate<String, CommandDto> kafkaTemplate;

    @Override
    public DeviceDto register(DeviceDto device) {
        Device d = devicesRepository.save(convertFromDto(device));
        return convertToDto(d);
    }

    @Override
    public DeviceDto getDeviceInfo(UUID deviceId) {
        Device device = devicesRepository.findById(deviceId)
                .orElseThrow(() -> new RollbackException("Device not found"));
        return convertToDto(device);
    }

    @Override
    public void updateDeviceStatus(UUID deviceId, String status) {
        Device device = devicesRepository.findById(deviceId)
                .orElseThrow(() -> new RollbackException("Device not found"));
        device.setStatus(status);
        devicesRepository.save(device);
    }

    @Override
    public void sendDeviceCommand(UUID deviceId, CommandDto command) {
        Device device = devicesRepository.findById(deviceId)
                .orElseThrow(() -> new RollbackException("Device not found"));
        switch (command.getType()) {
            case "ON" -> device.setStatus("ON");
            case "OFF" -> device.setStatus("OFF");
        }
        command.setDeviceId(deviceId);
        kafkaTemplate.send("devices", command);
        devicesRepository.save(device);
    }

    private Device convertFromDto(DeviceDto device) {
        Device d = new Device();
        d.setDeviceId(device.getDeviceId());
        d.setName(device.getName());
        d.setStatus(device.getStatus());
        return d;
    }

    private DeviceDto convertToDto(Device device) {
        DeviceDto d = new DeviceDto();
        d.setDeviceId(device.getDeviceId());
        d.setName(device.getName());
        d.setStatus(device.getStatus());
        return d;
    }
}