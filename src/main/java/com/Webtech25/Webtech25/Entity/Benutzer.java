package com.Webtech25.Webtech25.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "benutzer")
@EntityListeners(AuditingEntityListener.class)
public class Benutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 6)
    private String passwort;

    @NotBlank
    private String vorname;

    @NotBlank
    private String nachname;

    @Enumerated(EnumType.STRING)
    private Rolle rolle = Rolle.BENUTZER;

    @OneToMany(mappedBy = "benutzer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Kleidungsstueck> kleidungsstuecke;

    @CreatedDate
    private LocalDateTime erstelltAm;

    @LastModifiedDate
    private LocalDateTime aktualisiertAm;

    public enum Rolle {
        BENUTZER, ADMIN
    }

    // Konstruktoren
    public Benutzer() {}

    public Benutzer(String email, String passwort, String vorname, String nachname) {
        this.email = email;
        this.passwort = passwort;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    // Getter und Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswort() { return passwort; }
    public void setPasswort(String passwort) { this.passwort = passwort; }

    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }

    public String getNachname() { return nachname; }
    public void setNachname(String nachname) { this.nachname = nachname; }

    public Rolle getRolle() { return rolle; }
    public void setRolle(Rolle rolle) { this.rolle = rolle; }

    public List<Kleidungsstueck> getKleidungsstuecke() { return kleidungsstuecke; }
    public void setKleidungsstuecke(List<Kleidungsstueck> kleidungsstuecke) { this.kleidungsstuecke = kleidungsstuecke; }

    public LocalDateTime getErstelltAm() { return erstelltAm; }
    public void setErstelltAm(LocalDateTime erstelltAm) { this.erstelltAm = erstelltAm; }

    public LocalDateTime getAktualisiertAm() { return aktualisiertAm; }
    public void setAktualisiertAm(LocalDateTime aktualisiertAm) { this.aktualisiertAm = aktualisiertAm; }

    public String getVollstaendigerName() {
        return vorname + " " + nachname;
    }
}