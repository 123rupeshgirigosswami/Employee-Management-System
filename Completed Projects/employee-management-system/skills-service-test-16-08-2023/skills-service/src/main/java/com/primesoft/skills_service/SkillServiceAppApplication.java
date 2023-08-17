package com.primesoft.skills_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement

//Updated  17-08-2023
public class SkillServiceAppApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(SkillServiceAppApplication.class);

	/**
	 * Main method to start the Skill Service application.
	 *
	 * @param args Command-line arguments
	 */
	public static void main(String[] args) {
		LOGGER.info("Starting Skill Service application...");
		SpringApplication.run(SkillServiceAppApplication.class, args);
		LOGGER.info("Skill Service application started successfully.");
	}
}
