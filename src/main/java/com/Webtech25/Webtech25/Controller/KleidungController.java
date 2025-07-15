package com.Webtech25.Webtech25.Controller;

import com.Webtech25.Webtech25.Entity.Benutzer;
import com.Webtech25.Webtech25.Repository.KleidungsstueckRepository;
import com.Webtech25.Webtech25.Service.BenutzerService;
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
public class KleidungController {

    private final KleidungsstueckRepository kleidungsstueckRepository;
    private final BenutzerService benutzerService;

    @Autowired
    public KleidungController(KleidungsstueckRepository repository, BenutzerService benutzerService) {
        this.kleidungsstueckRepository = repository;
        this.benutzerService = benutzerService;
    }

    @GetMapping
    public ResponseEntity<List<Kleidungsstueck>> getAll(HttpSession session) {
        Long benutzerId = (Long) session.getAttribute("benutzerId");
        if (benutzerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Benutzer benutzer = benutzerService.findeNachId(benutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(kleidungsstueckRepository.findByBenutzer(benutzer));
    }

    @PostMapping
    public ResponseEntity<Kleidungsstueck> addKleidung(@RequestBody Kleidungsstueck kleidungsstueck, HttpSession session) {
        Long benutzerId = (Long) session.getAttribute("benutzerId");
        if (benutzerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Benutzer benutzer = benutzerService.findeNachId(benutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        kleidungsstueck.setBenutzer(benutzer);
        return ResponseEntity.ok(kleidungsstueckRepository.save(kleidungsstueck));
    }

    // Kleidung umlagern (zB. Lager ändern)
    @PutMapping("/{id}")
    public ResponseEntity<Kleidungsstueck> updateLager(@PathVariable Long id, @RequestBody Kleidungsstueck updated, HttpSession session) {
        Long benutzerId = (Long) session.getAttribute("benutzerId");
        if (benutzerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return kleidungsstueckRepository.findById(id)
                .map(k -> {
                    k.setLager(updated.getLager());
                    return ResponseEntity.ok(kleidungsstueckRepository.save(k));
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kleidung nicht gefunden"));
    }

    // Kleidung aus dem Lager entfernen
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKleidung(@PathVariable Long id, HttpSession session) {
        Long benutzerId = (Long) session.getAttribute("benutzerId");
        if (benutzerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        kleidungsstueckRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}