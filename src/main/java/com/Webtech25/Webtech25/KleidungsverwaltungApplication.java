
package com.Webtech25.Webtech25;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;  // Korrigiert von "EnableJpAvoidIting"

@SpringBootApplication
@EnableJpaAuditing  // Korrigiert
public class KleidungsverwaltungApplication {
    public static void main(String[] args) {
        SpringApplication.run(KleidungsverwaltungApplication.class, args);
    }

//    @Bean
//    CommandLineRunner initDatabase(KleidungsstueckRepository kleidungRepo) {
//        return args -> {
//            kleidungRepo.save(new Kleidungsstueck("T-Shirt", "L","Lager A", "Ibrahim"));
//            kleidungRepo.save(new Kleidungsstueck("Hose", "M","Lager B", "Ibrahim"));
//        };
//    }
}