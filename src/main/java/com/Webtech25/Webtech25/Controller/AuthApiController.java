package com.Webtech25.Webtech25.Controller;

import com.Webtech25.Webtech25.Entity.Benutzer;
import com.Webtech25.Webtech25.Service.BenutzerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    private BenutzerService benutzerService;

    @PostMapping("/anmelden")
    public ResponseEntity<Map<String, Object>> apiLogin(
            @RequestParam String email,
            @RequestParam String passwort,
            HttpSession session
    ) {
        Optional<Benutzer> benutzer = benutzerService.validiereAnmeldedaten(email, passwort);
        Map<String, Object> response = new HashMap<>();

        if (benutzer.isPresent()) {
            session.setAttribute("benutzerId", benutzer.get().getId()); // Nur ID speichern (minimale Session-Daten)

            response.put("success", true);
            response.put("user", Map.of( // Nur notwendige Benutzerdaten zurückgeben
                    "id", benutzer.get().getId(),
                    "email", benutzer.get().getEmail(),
                    "vorname", benutzer.get().getVorname()
            ));

            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("error", "Ungültige Anmeldedaten");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/abmelden")
    public ResponseEntity<Map<String, Boolean>> apiLogout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("success", true));
    }
}