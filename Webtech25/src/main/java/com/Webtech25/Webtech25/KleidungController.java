package com.Webtech25.Webtech25;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/kleidung")
public class KleidungController {

    private  KleidungRepository kleidungRepo;

    public KleidungController(KleidungRepository repository) {
        this.kleidungRepo = repository;
    }

    // Alle Kleidungsstücke abrufen
    @GetMapping
    public List<Kleidung> getAll() {
        return kleidungRepo.findAll();
    }

    // Kleidung einlagern
    @PostMapping
    public Kleidung addKleidung(@RequestBody Kleidung kleidung) {
        return kleidungRepo.save(kleidung);
    }

    // Kleidung umlagern (zB. Lager ändern)
    @PutMapping("/{id}")
    public Kleidung updateLager(@PathVariable Long id, @RequestBody Kleidung updated) {
        return kleidungRepo.findById(id).map(k -> {
            k.setLager(updated.getLager());
            return kleidungRepo.save(k);
        }).orElseThrow(() -> new RuntimeException("Kleidung nicht gefunden"));
    }

    // Kleidung aus dem Lager entfernen
    @DeleteMapping("/{id}")
    public void deleteKleidung(@PathVariable Long id) {
        kleidungRepo.deleteById(id);
    }
}
