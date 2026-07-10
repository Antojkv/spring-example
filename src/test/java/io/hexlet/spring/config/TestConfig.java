package io.hexlet.spring.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public Validator validator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            return factory.getValidator();
        }
    }

    @Bean
    @Primary
    public LocalValidatorFactoryBean validatorFactory() {
        return new LocalValidatorFactoryBean() {
            @Override
            public void afterPropertiesSet() {
                // Отключаем валидацию для тестов
            }
        };
    }
}
