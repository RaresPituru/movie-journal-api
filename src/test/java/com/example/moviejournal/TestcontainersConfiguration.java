package com.example.moviejournal;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Starts a real PostgreSQL in Docker for integration tests.
 * Declared as a bean, so Spring manages its lifecycle: one container is shared by every
 * test class that uses the same (cached) application context.
 * {@link ServiceConnection} points spring.datasource.* at the container automatically.
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgresContainer() {
        return new PostgreSQLContainer("postgres:15-alpine"); // same image as docker-compose.yml
    }
}
