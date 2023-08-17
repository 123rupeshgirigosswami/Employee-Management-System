package com.primesoft.skills_service.config;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for ModelMapper.
 */
@Configuration
public class ModelMapperConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelMapperConfig.class);

    /**
     * Creates a new instance of ModelMapper and configures it.
     *
     * @return The configured ModelMapper instance.
     */
    @Bean
    public ModelMapper modelMapper() {
        LOGGER.info("Initializing ModelMapper configuration...");

        ModelMapper modelMapper = new ModelMapper();
        // Custom configurations can be added here

        LOGGER.info("ModelMapper configuration initialized successfully.");
        return modelMapper;
    }
}
