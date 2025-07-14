package com.Webtech25.Webtech25.Service;

import com.Webtech25.Webtech25.Entity.Benutzer;
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

    @Autowired
    private BenutzerRepository benutzerRepository;

    @Autowired
    private PasswordEncoder passwortEncoder;

    public Benutzer registriereBenutzer(String email, String passwort, String vorname, String nachname) {
        if (benutzerRepository.existsByEmail(email)) {
            throw new RuntimeException("Diese E-Mail ist bereits registriert");
        }

        Benutzer benutzer = new Benutzer(email, passwortEncoder.encode(passwort), vorname, nachname);
        return benutzerRepository.save(benutzer);
    }

    public Optional<Benutzer> findeNachEmail(String email) {
        return benutzerRepository.findByEmail(email);
    }

    public boolean validierePasswort(Benutzer benutzer, String rohesPasswort) {
        return passwortEncoder.matches(rohesPasswort, benutzer.getPasswort());
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

    public void loescheBenutzer(Long id) {
        benutzerRepository.deleteById(id);
    }

    public boolean istEmailVergeben(String email) {
        return benutzerRepository.existsByEmail(email);
    }

    public Benutzer aenderePasswort(Benutzer benutzer, String neuesPasswort) {
        benutzer.setPasswort(passwortEncoder.encode(neuesPasswort));
        return benutzerRepository.save(benutzer);
    }
}