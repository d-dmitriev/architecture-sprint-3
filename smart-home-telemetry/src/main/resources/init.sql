CREATE TABLE IF NOT EXISTS temperature_sensors (
    id UUID PRIMARY KEY,
    current_temperature DOUBLE PRECISION NOT NULL,
    last_updated TIMESTAMP NOT NULL
);
