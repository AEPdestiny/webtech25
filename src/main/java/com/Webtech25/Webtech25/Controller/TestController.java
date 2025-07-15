package com.Webtech25.Webtech25.Controller;

import com.Webtech25.Webtech25.Entity.Benutzer;
import com.Webtech25.Webtech25.Repository.BenutzerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final BenutzerRepository benutzerRepository;

    public TestController(BenutzerRepository benutzerRepository) {
        this.benutzerRepository = benutzerRepository;
    }

    @GetMapping("/create-test-user")
    public String testSave() {
        Benutzer test = new Benutzer();
        test.setEmail("test@example.com");
        test.setPasswort("test123"); // Wird in Produktion gehasht werden
        benutzerRepository.save(test);
        return "Testbenutzer erstellt. Gesamt: " + benutzerRepository.count();
    }
}