package ru.yandex.practicum.smarthome.service;

import ru.yandex.practicum.smarthome.dto.DeviceDto;

import java.util.UUID;

public interface DevicesService {
    DeviceDto register(DeviceDto device);
    DeviceDto getDeviceInfo(UUID deviceId);
    void updateDeviceStatus(UUID deviceId, String status);
    void sendDeviceCommand(UUID deviceId, String command);
}
