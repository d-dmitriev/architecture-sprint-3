package ru.yandex.practicum.smarthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.smarthome.entity.Telemetry;

import java.util.List;
import java.util.UUID;

@Repository
public interface TelemetryRepository extends JpaRepository<Telemetry, UUID> {
    Telemetry findFirstByDeviceIdOrderByLastUpdatedDesc(@Param("device_id") UUID deviceId);
    List<Telemetry> findAllByDeviceId(@Param("device_id") UUID deviceId);
}