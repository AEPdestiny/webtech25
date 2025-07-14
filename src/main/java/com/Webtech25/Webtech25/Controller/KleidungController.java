package com.Webtech25.Webtech25.Controller;

import com.Webtech25.Webtech25.Repository.KleidungsstueckRepository;
import org.springframework.web.bind.annotation.*;
import com.Webtech25.Webtech25.Entity.Kleidungsstueck;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/kleidung")
public class KleidungController {

    private KleidungsstueckRepository kleidungstueckRepository;

    public KleidungController(KleidungsstueckRepository repository) {
        this.kleidungstueckRepository = repository;
    }

    // Alle Kleidungsstücke abrufen
    @GetMapping
    public List<Kleidungsstueck> getAll() {
        return kleidungstueckRepository.findAll();
    }

    // Kleidung einlagern
    @PostMapping
    public Kleidungsstueck addKleidung(@RequestBody Kleidungsstueck kleidungsstueck) {
        return kleidungstueckRepository.save(kleidungsstueck);
    }

    // Kleidung umlagern (zB. Lager ändern)
    @PutMapping("/{id}")
    public Kleidungsstueck updateLager(@PathVariable Long id, @RequestBody Kleidungsstueck updated) {
        return kleidungstueckRepository.findById(id).map(k -> {
            k.setLager(updated.getLager());
            return kleidungstueckRepository.save(k);
        }).orElseThrow(() -> new RuntimeException("Kleidung nicht gefunden"));
    }

    // Kleidung aus dem Lager entfernen
    @DeleteMapping("/{id}")
    public void deleteKleidung(@PathVariable Long id) {
        kleidungstueckRepository.deleteById(id);
    }
}
