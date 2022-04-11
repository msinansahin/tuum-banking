package com.tuum.banking;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresqlContainerContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final PostgreSQLContainer databaseContainer;

    static {
        databaseContainer = (PostgreSQLContainer) (new PostgreSQLContainer("postgres:9.6.12")
                .withUsername("banking")
                .withPassword("banking")
                .withDatabaseName("local_banking")
                .withReuse(true));
        databaseContainer.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of("spring.datasource.url=" + databaseContainer.getJdbcUrl())
                .and("spring.datasource.password=" + databaseContainer.getPassword())
                .and("spring.datasource.username=" + databaseContainer.getUsername())
                .applyTo(applicationContext.getEnvironment());
    }
}
