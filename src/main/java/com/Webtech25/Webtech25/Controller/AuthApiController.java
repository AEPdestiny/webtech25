package com.Webtech25.Webtech25.Controller;

import com.Webtech25.Webtech25.Entity.Benutzer;
import com.Webtech25.Webtech25.Service.BenutzerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    private BenutzerService benutzerService;

    @PostMapping("/anmelden")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam String email,
            @RequestParam String passwort,
            HttpSession session
    ) {
        // 1. Authentifizierung
        Optional<Benutzer> benutzer = benutzerService.validiereAnmeldedaten(email, passwort);

        if (benutzer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "error", "E-Mail oder Passwort ungültig" // Präzisere Fehlermeldung
            ));
        }

        // 2. Session speichern
        Benutzer loggedInUser = benutzer.get();
        session.setAttribute("benutzerId", loggedInUser.getId());

        // 3. Response für Frontend
        return ResponseEntity.ok(Map.of(
                "success", true,
                "user", Map.of(
                        "id", loggedInUser.getId(),
                        "email", loggedInUser.getEmail(),
                        "vorname", loggedInUser.getVorname(),
                        "nachname", loggedInUser.getNachname() // Falls benötigt
                ),
                "message", "Login erfolgreich"
        ));
    }

    @GetMapping("/session-check")
    public ResponseEntity<Map<String, Object>> checkSession(HttpSession session) {
        Long benutzerId = (Long) session.getAttribute("benutzerId");

        if (benutzerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "authenticated", false
            ));
        }

        return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "benutzerId", benutzerId
        ));
    }

    @PostMapping("/abmelden")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Logout erfolgreich"
        ));
    }
}