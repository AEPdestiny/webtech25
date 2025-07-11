package com.Webtech25.Webtech25.Repository;

import com.Webtech25.Webtech25.Entity.Benutzer;
import com.Webtech25.Webtech25.Entity.Kleidungsstueck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KleidungsstueckRepository extends JpaRepository<Kleidungsstueck, Long> {

    List<Kleidungsstueck> findByBenutzer(Benutzer benutzer);

    List<Kleidungsstueck> findByBenutzerAndBezeichnungContainingIgnoreCase(Benutzer benutzer, String bezeichnung);

    List<Kleidungsstueck> findByBenutzerAndGroesse(Benutzer benutzer, Kleidungsstueck.Groesse groesse);

    List<Kleidungsstueck> findByBenutzerAndKategorie(Benutzer benutzer, Kleidungsstueck.Kategorie kategorie);

    @Query("SELECT k FROM Kleidungsstueck k WHERE k.benutzer = :benutzer AND k.lagerbestand <= :grenzwert")
    List<Kleidungsstueck> findeArtikelMitNiedrigemLagerbestand(
            @Param("benutzer") Benutzer benutzer,
            @Param("grenzwert") int grenzwert);

    @Query("SELECT k FROM Kleidungsstueck k WHERE k.benutzer = :benutzer AND " +
            "(k.bezeichnung LIKE %:suche% OR k.beschreibung LIKE %:suche% OR k.farbe LIKE %:suche%)")
    List<Kleidungsstueck> sucheKleidungsstuecke(
            @Param("benutzer") Benutzer benutzer,
            @Param("suche") String suche);

    @Query("SELECT SUM(k.lagerbestand) FROM Kleidungsstueck k WHERE k.benutzer = :benutzer")
    Integer berechneGesamtLagerbestandNachBenutzer(@Param("benutzer") Benutzer benutzer);

    @Query("SELECT COUNT(k) FROM Kleidungsstueck k WHERE k.benutzer = :benutzer AND k.lagerbestand = 0")
    Long zaehleAusverkaufteArtikel(@Param("benutzer") Benutzer benutzer);
}

