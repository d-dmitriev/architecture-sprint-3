package ru.yandex.practicum.smarthome.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.yandex.practicum.smarthome.entity.TemperatureSensor;
import ru.yandex.practicum.smarthome.repository.TemperatureSensorRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class TemperatureSensorControllerTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TemperatureSensorRepository temperatureSensorRepository;

    @Test
    void testGetCurrentTemperature() throws Exception {
        TemperatureSensor temperatureSensor = new TemperatureSensor();
        temperatureSensor.setCurrentTemperature(20.0);
        temperatureSensor.setLastUpdated(LocalDateTime.now());
        temperatureSensor = temperatureSensorRepository.save(temperatureSensor);

        mockMvc.perform(get("/api/temperature/{id}/current-temperature", temperatureSensor.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("20.0"));
    }

    @Test
    void testSetCurrentTemperature() throws Exception {
        TemperatureSensor temperatureSensor = new TemperatureSensor();
        temperatureSensor.setCurrentTemperature(20.0);
        temperatureSensor.setLastUpdated(LocalDateTime.now());
        temperatureSensor = temperatureSensorRepository.save(temperatureSensor);

        mockMvc.perform(post("/api/temperature/{id}/set-temperature", temperatureSensor.getId())
                        .param("temperature", "20.0"))
                .andExpect(status().isNoContent());

        TemperatureSensor updatedTemperatureSensor = temperatureSensorRepository.findById(temperatureSensor.getId())
                .orElseThrow(() -> new RuntimeException("HeatingSystem not found"));
        assertEquals(20.0, updatedTemperatureSensor.getCurrentTemperature(), 0.01);
    }
}
