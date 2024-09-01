package ru.yandex.practicum.smarthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.smarthome.entity.Device;

import java.util.UUID;

@Repository
public interface DevicesRepository extends JpaRepository<Device, UUID> {
}
