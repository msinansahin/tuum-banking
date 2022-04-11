package com.tuum.banking;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.RabbitMQContainer;

import lombok.SneakyThrows;

public class RabbitmqContainerContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static RabbitMQContainer container;

    static {
        container = new RabbitMQContainer("rabbitmq:3.7.25-management-alpine")
                .withQueue("banking.account")
                .withQueue("banking.transaction");
        container.start();
    }

    @SneakyThrows
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of("spring.rabbitmq.port=" + container.getAmqpPort())
                .and("spring.rabbitmq.password=" + container.getAdminPassword())
                .and("spring.rabbitmq.username=" + container.getAdminUsername())
                .applyTo(applicationContext.getEnvironment());
    }
}
