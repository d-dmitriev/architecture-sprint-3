package ru.yandex.practicum.smarthome.service;

import ru.yandex.practicum.smarthome.dto.TemperatureSensorDto;

public interface TemperatureSensorService {
    TemperatureSensorDto initTemperatureSensor(TemperatureSensorDto temperatureSensor);
    void setCurrentTemperature(Long id, double temperature);
    Double getCurrentTemperature(Long id);
}
