package ru.yandex.practicum.smarthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.smarthome.entity.HeatingSystem;

import java.util.UUID;

@Repository
public interface HeatingSystemRepository extends JpaRepository<HeatingSystem, Long> {
    HeatingSystem findFirstByDeviceId(@Param("device_id") UUID deviceId);
}
