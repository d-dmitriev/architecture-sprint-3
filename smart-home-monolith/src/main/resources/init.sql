CREATE TABLE IF NOT EXISTS heating_systems (
    id BIGSERIAL PRIMARY KEY,
    is_on BOOLEAN NOT NULL,
    target_temperature DOUBLE PRECISION NOT NULL,
    current_temperature DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS temperature_sensors (
    id BIGSERIAL PRIMARY KEY,
    device_id UUID NOT NULL,
    current_temperature DOUBLE PRECISION NOT NULL,
    last_updated TIMESTAMP NOT NULL
);
