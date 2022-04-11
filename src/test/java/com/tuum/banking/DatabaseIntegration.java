package com.tuum.banking;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ContextConfiguration(initializers = { PostgresqlContainerContextInitializer.class })
@Transactional
public @interface DatabaseIntegration {
}
