package com.Webtech25.Webtech25;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Webtech25Application {

	public static void main(String[] args) {
		SpringApplication.run(Webtech25Application.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(KleidungRepository kleidungRepo) {
		return args -> {
			kleidungRepo.save(new Kleidung("T-Shirt", "L","Lager A"));
			kleidungRepo.save(new Kleidung("Hose", "M","Lager B"));
		};
	}
}
