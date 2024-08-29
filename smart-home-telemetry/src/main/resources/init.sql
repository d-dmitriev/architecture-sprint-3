CREATE TABLE IF NOT EXISTS telemetry (
    id UUID PRIMARY KEY,
    device_id UUID NOT NULL,
    current_value_double DOUBLE PRECISION,
    current_value_integer INTEGER,
    current_value_string TEXT,
    last_updated TIMESTAMP NOT NULL
);
