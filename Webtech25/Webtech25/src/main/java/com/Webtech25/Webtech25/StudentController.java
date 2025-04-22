package com.Webtech25.Webtech25;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kleidung")
public class KleidungController {

    private  KleidungRepository repository;

    public KleidungController(KleidungRepository repository) {
        this.repository = repository;
    }

    // Alle Kleidungsstücke abrufen
    @GetMapping
    public List<Kleidung> getAll() {
        return repository.findAll();
    }

    // Kleidung einlagern
    @PostMapping
    public Kleidung addKleidung(@RequestBody Kleidung kleidung) {
        return repository.save(kleidung);
    }

    // Kleidung umlagern (zB. Lager ändern)
    @PutMapping("/{id}")
    public Kleidung updateLager(@PathVariable Long id, @RequestBody Kleidung updated) {
        return repository.findById(id).map(k -> {
            k.setLager(updated.getLager());
            return repository.save(k);
        }).orElseThrow(() -> new RuntimeException("Kleidung nicht gefunden"));
    }

    // Kleidung aus dem Lager entfernen
    @DeleteMapping("/{id}")
    public void deleteKleidung(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
