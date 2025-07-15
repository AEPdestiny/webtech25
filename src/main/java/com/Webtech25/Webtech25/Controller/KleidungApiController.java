package com.Webtech25.Webtech25.Controller;

import com.Webtech25.Webtech25.Entity.Benutzer;
import com.Webtech25.Webtech25.Repository.KleidungsstueckRepository;
import com.Webtech25.Webtech25.Service.BenutzerService;
import com.Webtech25.Webtech25.Service.KleidungsstueckService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Webtech25.Webtech25.Entity.Kleidungsstueck;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/kleidung")
public class KleidungApiController {

    private final KleidungsstueckRepository kleidungsstueckRepository;

    @Autowired
    private BenutzerService benutzerService; // Service injizieren

    @Autowired
    public KleidungApiController(KleidungsstueckRepository repository) {
        this.kleidungsstueckRepository = repository;
    }

    @GetMapping("/user")
    public ResponseEntity<List<Kleidungsstueck>> getKleidungByUser(HttpSession session) {
        Long benutzerId = (Long) session.getAttribute("benutzerId");
        if (benutzerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Benutzer benutzer = benutzerService.findeNachId(benutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(kleidungsstueckRepository.findByBenutzer(benutzer));
    }
}