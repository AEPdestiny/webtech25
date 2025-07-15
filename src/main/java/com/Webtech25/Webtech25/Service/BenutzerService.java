package com.Webtech25.Webtech25.Service;

import com.Webtech25.Webtech25.Entity.Benutzer;
import com.Webtech25.Webtech25.Exceptions.EmailExistsException;
import com.Webtech25.Webtech25.Repository.BenutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BenutzerService {

    private final BenutzerRepository benutzerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BenutzerService(BenutzerRepository benutzerRepository,
                           PasswordEncoder passwordEncoder) {
        this.benutzerRepository = benutzerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registriert einen neuen Benutzer
     * @throws EmailExistsException wenn die Email bereits existiert
     */
    public Benutzer registriereBenutzer(String email, String passwort,
                                        String vorname, String nachname) {
        if (benutzerRepository.existsByEmail(email)) {
            throw new EmailExistsException("Diese E-Mail ist bereits registriert");
        }

        Benutzer benutzer = new Benutzer();
        benutzer.setEmail(email);
        benutzer.setPasswort(passwordEncoder.encode(passwort));
        benutzer.setVorname(vorname);
        benutzer.setNachname(nachname);

        return benutzerRepository.save(benutzer);
    }

    /**
     * Alternative Registrierungsmethode mit DTO-Überprüfung
     */
    public Benutzer registriereBenutzer(Benutzer benutzer) {
        if (benutzerRepository.existsByEmail(benutzer.getEmail())) {
            throw new EmailExistsException("Email bereits registriert");
        }

        benutzer.setPasswort(passwordEncoder.encode(benutzer.getPasswort()));
        return benutzerRepository.save(benutzer);
    }

    public Optional<Benutzer> findeNachEmail(String email) {
        return benutzerRepository.findByEmail(email);
    }

    public boolean validierePasswort(Benutzer benutzer, String rohesPasswort) {
        return passwordEncoder.matches(rohesPasswort, benutzer.getPasswort());
    }

    public Benutzer aktualisiereBenutzer(Benutzer benutzer) {
        return benutzerRepository.save(benutzer);
    }

    public List<Benutzer> findeAlleBenutzer() {
        return benutzerRepository.findAll();
    }

    public Optional<Benutzer> findeNachId(Long id) {
        return benutzerRepository.findById(id);
    }

    @Transactional
    public void loescheBenutzer(Long id) {
        benutzerRepository.deleteById(id);
    }

    public boolean istEmailVergeben(String email) {
        return benutzerRepository.existsByEmail(email);
    }

    @Transactional
    public Benutzer aenderePasswort(Long benutzerId, String neuesPasswort) {
        Benutzer benutzer = benutzerRepository.findById(benutzerId)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        benutzer.setPasswort(passwordEncoder.encode(neuesPasswort));
        return benutzerRepository.save(benutzer);
    }

    /**
     * Prüft Anmeldedaten und gibt Benutzer zurück wenn gültig
     */
    public Optional<Benutzer> validiereAnmeldedaten(String email, String passwort) {
        return findeNachEmail(email)
                .filter(benutzer -> validierePasswort(benutzer, passwort));
    }

    @Transactional
    public String createTestUser() {
        Benutzer test = new Benutzer();
        test.setEmail("test@example.com");
        test.setPasswort(passwordEncoder.encode("test123"));
        benutzerRepository.save(test);
        return "Testbenutzer erstellt (ID: " + test.getId() + ")";
    }
}