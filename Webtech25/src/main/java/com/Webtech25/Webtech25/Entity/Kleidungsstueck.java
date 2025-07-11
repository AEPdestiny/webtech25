package com.Webtech25.Webtech25.Entity;

import com.Webtech25.Webtech25.Entity.Benutzer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "kleidungsstuecke")
@EntityListeners(AuditingEntityListener.class)
public class Kleidungsstueck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "lager")
    private Long lager;

    @Min(0)
    @Column(name = "anzahl")
    private Integer lagerbestand=0;

    @NotBlank
    @Column(name = "name",nullable = false)
    private String bezeichnung;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Groesse groesse;


    private String beschreibung;

    private String farbe;

    @Enumerated(EnumType.STRING)
    private Kategorie kategorie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benutzer_id", nullable = false)
    private Benutzer benutzer;



    @CreatedDate
    private LocalDateTime erstelltAm;

    @LastModifiedDate
    private LocalDateTime aktualisiertAm;

    public enum Groesse {
        XS, S, M, L, XL, XXL, XXXL
    }

    public enum Kategorie {
        HEMD, HOSE, KLEID, JACKE, SCHUHE, ACCESSOIRES, SONSTIGES
    }

    // Konstruktoren
    public Kleidungsstueck() {}

    public Kleidungsstueck(String bezeichnung, Groesse groesse, Integer lagerbestand, Benutzer benutzer) {
        this.bezeichnung = bezeichnung;
        this.groesse = groesse;
        this.lager = lager;
        this.lagerbestand = lagerbestand;
        this.benutzer = benutzer;
    }

    // Getter und Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBezeichnung() { return bezeichnung; }
    public void setBezeichnung(String bezeichnung) { this.bezeichnung = bezeichnung; }

    public Groesse getGroesse() { return groesse; }
    public void setGroesse(Groesse groesse) { this.groesse = groesse; }

    public Long getLager() { return lager; }
    public void setLager(Long lager) { this.lager = lager; }

    public Integer getLagerbestand() { return lagerbestand; }
    public void setLagerbestand(Integer lagerbestand) { this.lagerbestand = lagerbestand; }

    public String getBeschreibung() { return beschreibung; }
    public void setBeschreibung(String beschreibung) { this.beschreibung = beschreibung; }

    public String getFarbe() { return farbe; }
    public void setFarbe(String farbe) { this.farbe = farbe; }

    public Kategorie getKategorie() { return kategorie; }
    public void setKategorie(Kategorie kategorie) { this.kategorie = kategorie; }

    public Benutzer getBenutzer() { return benutzer; }
    public void setBenutzer(Benutzer benutzer) { this.benutzer = benutzer; }

    public LocalDateTime getErstelltAm() { return erstelltAm; }
    public void setErstelltAm(LocalDateTime erstelltAm) { this.erstelltAm = erstelltAm; }

    public LocalDateTime getAktualisiertAm() { return aktualisiertAm; }
    public void setAktualisiertAm(LocalDateTime aktualisiertAm) { this.aktualisiertAm = aktualisiertAm; }

    // Hilfsmethoden
    public boolean istAufLager() {
        return lagerbestand > 0;
    }

    public boolean istNiedrigerLagerbestand() {
        return lagerbestand <= 5;
    }
}