package com.Webtech25.Webtech25.Service;

import com.Webtech25.Webtech25.Entity.Benutzer;
import com.Webtech25.Webtech25.Entity.Kleidungsstueck;
import com.Webtech25.Webtech25.Repository.KleidungsstueckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class KleidungsstueckService {

    @Autowired
    private KleidungsstueckRepository kleidungsstueckRepository;

    public Kleidungsstueck speichereKleidungsstueck(Kleidungsstueck kleidungsstueck) {
        validiereKleidungsstueck(kleidungsstueck);
        return kleidungsstueckRepository.save(kleidungsstueck);
    }

    public List<Kleidungsstueck> holeAlleKleidungsstueckeNachBenutzer(Benutzer benutzer) {
        return kleidungsstueckRepository.findByBenutzer(benutzer);
    }

    public Optional<Kleidungsstueck> findeNachId(Long id) {
        return kleidungsstueckRepository.findById(id);
    }

    public Kleidungsstueck aktualisiereKleidungsstueck(Kleidungsstueck kleidungsstueck) {
        validiereKleidungsstueck(kleidungsstueck);
        return kleidungsstueckRepository.save(kleidungsstueck);
    }

    public void loescheKleidungsstueck(Long id) {
        kleidungsstueckRepository.deleteById(id);
    }

    public List<Kleidungsstueck> sucheKleidungsstuecke(Benutzer benutzer, String suchbegriff) {
        if (suchbegriff == null || suchbegriff.trim().isEmpty()) {
            return holeAlleKleidungsstueckeNachBenutzer(benutzer);
        }
        return kleidungsstueckRepository.sucheKleidungsstuecke(benutzer, suchbegriff.trim());
    }

    public List<Kleidungsstueck> filterNachGroesse(Benutzer benutzer, Kleidungsstueck.Groesse groesse) {
        return kleidungsstueckRepository.findByBenutzerAndGroesse(benutzer, groesse);
    }

    public List<Kleidungsstueck> filterNachKategorie(Benutzer benutzer, Kleidungsstueck.Kategorie kategorie) {
        return kleidungsstueckRepository.findByBenutzerAndKategorie(benutzer, kategorie);
    }

    public List<Kleidungsstueck> holeArtikelMitNiedrigemLagerbestand(Benutzer benutzer) {
        return kleidungsstueckRepository.findeArtikelMitNiedrigemLagerbestand(benutzer, 5);
    }

    public Integer berechneGesamtLagerbestand(Benutzer benutzer) {
        Integer gesamt = kleidungsstueckRepository.berechneGesamtLagerbestandNachBenutzer(benutzer);
        return gesamt != null ? gesamt : 0;
    }

    public Long zaehleAusverkaufteArtikel(Benutzer benutzer) {
        return kleidungsstueckRepository.zaehleAusverkaufteArtikel(benutzer);
    }

    public Kleidungsstueck aktualisiereLagerbestand(Long kleidungsstueckId, Integer neuerLagerbestand) {
        Optional<Kleidungsstueck> optionalKleidungsstueck = kleidungsstueckRepository.findById(kleidungsstueckId);
        if (optionalKleidungsstueck.isPresent()) {
            Kleidungsstueck kleidungsstueck = optionalKleidungsstueck.get();
            kleidungsstueck.setLagerbestand(neuerLagerbestand);
            return kleidungsstueckRepository.save(kleidungsstueck);
        }
        throw new RuntimeException("Kleidungsstück nicht gefunden");
    }

    private void validiereKleidungsstueck(Kleidungsstueck kleidungsstueck) {
        if (kleidungsstueck.getBezeichnung() == null || kleidungsstueck.getBezeichnung().trim().isEmpty()) {
            throw new RuntimeException("Bezeichnung ist erforderlich");
        }
        if (kleidungsstueck.getGroesse() == null) {
            throw new RuntimeException("Größe ist erforderlich");
        }
        if (kleidungsstueck.getLagerbestand() == null || kleidungsstueck.getLagerbestand() < 0) {
            throw new RuntimeException("Lagerbestand darf nicht negativ sein");
        }
        if (kleidungsstueck.getBenutzer() == null) {
            throw new RuntimeException("Benutzer ist erforderlich");
        }
    }
}