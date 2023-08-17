package com.primesoft.employee_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The main class to start the Employee Service application. This application
 * manages employee details and tasks. Author: Rupesh Giri
 */
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = "com.primesoft.employee_service")
public class EmployeeServiceApplication {

	/**
	 * The main method to start the application.
	 *
	 * @param args The command-line arguments.
	 */
	public static void main(String[] args) {
		SpringApplication.run(EmployeeServiceApplication.class, args);
	}
}
