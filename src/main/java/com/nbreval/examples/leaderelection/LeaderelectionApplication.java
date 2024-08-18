package com.nbreval.examples.leaderelection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // Configuration annotation needed to activate Spring Boot's scheduling
@SpringBootApplication
public class LeaderelectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaderelectionApplication.class, args);
	}

}
