package com.Webtech25.Webtech25.Controller;

import com.Webtech25.Webtech25.Entity.Benutzer;
import com.Webtech25.Webtech25.Exceptions.EmailExistsException;
import com.Webtech25.Webtech25.Service.BenutzerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthentifizierungsController {

    private final BenutzerService benutzerService;

    @Autowired
    public AuthentifizierungsController(BenutzerService benutzerService) {
        this.benutzerService = benutzerService;
    }

    @GetMapping("/anmelden")
    public String anmeldeFormular(Model model) {
        model.addAttribute("benutzer", new Benutzer());
        return "auth/anmelden";
    }

    @PostMapping("/anmelden")
    public String anmelden(@RequestParam String email,
                           @RequestParam String passwort,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {

        Optional<Benutzer> optionalBenutzer = benutzerService.validiereAnmeldedaten(email, passwort);
        if (optionalBenutzer.isPresent()) {
            Benutzer benutzer = optionalBenutzer.get();
            session.setAttribute("benutzer", benutzer);
            session.setAttribute("benutzerId", benutzer.getId());
            return "redirect:/dashboard";
        }

        redirectAttributes.addFlashAttribute("error", "Ungültige Anmeldedaten");
        return "redirect:/auth/anmelden";
    }

    @GetMapping("/registrieren")
    public String registrierungsFormular(Model model) {
        model.addAttribute("benutzer", new Benutzer());
        return "auth/registrieren";
    }

    @PostMapping("/registrieren")
    public String registrieren(@Valid @ModelAttribute("benutzer") Benutzer benutzer,
                               BindingResult bindingResult,
                               @RequestParam String passwortBestaetigen,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "auth/registrieren";
        }

        if (!benutzer.getPasswort().equals(passwortBestaetigen)) {
            bindingResult.rejectValue("passwort", "error.passwort", "Passwörter stimmen nicht überein");
            return "auth/registrieren";
        }

        try {
            benutzerService.registriereBenutzer(benutzer);
            redirectAttributes.addFlashAttribute("success", "Registrierung erfolgreich! Bitte melden Sie sich an.");
            return "redirect:/auth/anmelden";
        } catch (EmailExistsException e) {
            bindingResult.rejectValue("email", "error.email", e.getMessage());
            return "auth/registrieren";
        }
    }

    @GetMapping("/current-user")
    @ResponseBody
    public ResponseEntity<Benutzer> getCurrentUser(HttpSession session) {
        Long benutzerId = (Long) session.getAttribute("benutzerId");
        if (benutzerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return benutzerService.findeNachId(benutzerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/abmelden")
    public String abmelden(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/anmelden";
    }

}