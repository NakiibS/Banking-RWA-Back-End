package com.RWA.rwa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class RwaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RwaApplication.class, args);
	}
}
