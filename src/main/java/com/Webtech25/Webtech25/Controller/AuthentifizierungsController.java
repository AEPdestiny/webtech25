package com.Webtech25.Webtech25.Controller;

import com.Webtech25.Webtech25.Entity.Benutzer;
import com.Webtech25.Webtech25.Service.BenutzerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthentifizierungsController {

    @Autowired
    private BenutzerService benutzerService;

    @GetMapping("/anmelden")
    public String anmeldeFormular(Model model) {
        model.addAttribute("benutzer", new Benutzer());
        return "auth/anmelden";
    }

    @PostMapping("/anmelden")
    public String anmelden(@RequestParam String email,
                           @RequestParam String passwort,
                           HttpSession session,
                           RedirectAttributes umleitungsAttribute) {

        Optional<Benutzer> optionalBenutzer = benutzerService.findeNachEmail(email);
        if (optionalBenutzer.isPresent()) {
            Benutzer benutzer = optionalBenutzer.get();
            if (benutzerService.validierePasswort(benutzer, passwort)) {
                session.setAttribute("benutzer", benutzer);
                session.setAttribute("benutzerId", benutzer.getId());
                return "redirect:/dashboard";
            }
        }

        umleitungsAttribute.addFlashAttribute("fehler", "Ungültige Anmeldedaten");
        return "redirect:/auth/anmelden";
    }

    @GetMapping("/registrieren")
    public String registrierungsFormular(Model model) {
        model.addAttribute("benutzer", new Benutzer());
        return "auth/registrieren";
    }

    @PostMapping("/registrieren")
    public String registrieren(@Valid @ModelAttribute Benutzer benutzer,
                               BindingResult bindingResult,
                               @RequestParam String passwortBestaetigen,
                               RedirectAttributes umleitungsAttribute) {

        if (bindingResult.hasErrors()) {
            return "auth/registrieren";
        }

        if (!benutzer.getPasswort().equals(passwortBestaetigen)) {
            bindingResult.rejectValue("passwort", "fehler.passwort", "Passwörter stimmen nicht überein");
            return "auth/registrieren";
        }

        try {
            benutzerService.registriereBenutzer(benutzer.getEmail(), benutzer.getPasswort(),
                    benutzer.getVorname(), benutzer.getNachname());
            umleitungsAttribute.addFlashAttribute("erfolg", "Registrierung erfolgreich! Bitte melden Sie sich an.");
            return "redirect:/auth/anmelden";
        } catch (RuntimeException e) {
            bindingResult.rejectValue("email", "fehler.email", e.getMessage());
            return "auth/registrieren";
        }
    }

    @GetMapping("/abmelden")
    public String abmelden(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/anmelden";
    }
}