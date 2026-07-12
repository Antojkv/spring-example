package io.hexlet.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class ProfileConfig {

    private static final Logger logger = LoggerFactory.getLogger(ProfileConfig.class);

    @PostConstruct
    public void logActiveProfile() {
        String profile = System.getProperty("spring.profiles.active", "default");
        logger.info("========================================");
        logger.info("Active Spring Profile: {}", profile);
        logger.info("========================================");
    }
}
