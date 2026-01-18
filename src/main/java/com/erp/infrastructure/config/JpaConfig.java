package com.erp.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA configuration.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.erp.infrastructure.persistence.repository")
@EnableTransactionManagement
public class JpaConfig {
}
