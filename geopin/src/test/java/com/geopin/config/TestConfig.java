package com.geopin.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestConfig {
    public static final PostgreSQLContainer<?> postgres;

    static {
        // Create a Docker image name and explicitly declare compatibility with PostgreSQL
        DockerImageName postgisImage = DockerImageName
            .parse("postgis/postgis:15-3.3")
            .asCompatibleSubstituteFor("postgres");

        // Create and configure the container with the compatible image
        postgres = new PostgreSQLContainer<>(postgisImage)
            .withDatabaseName("geopin_test")
            .withUsername("test")
            .withPassword("test")
            .withCommand("postgres -c max_prepared_transactions=100");
        
        postgres.start();
    }
}