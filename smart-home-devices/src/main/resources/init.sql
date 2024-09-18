CREATE TABLE IF NOT EXISTS devices (
    device_id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    status TEXT NOT NULL
);

