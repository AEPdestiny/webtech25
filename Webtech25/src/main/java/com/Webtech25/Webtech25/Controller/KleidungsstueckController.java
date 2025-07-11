package com.Webtech25.Webtech25.Controller;

import com.Webtech25.Webtech25.Entity.Benutzer;
import com.Webtech25.Webtech25.Entity.Kleidungsstueck;
import com.Webtech25.Webtech25.Service.BenutzerService;
import com.Webtech25.Webtech25.Service.KleidungsstueckService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class KleidungsstueckController {

    @Autowired
    private KleidungsstueckService kleidungsstueckService;

    @Autowired
    private BenutzerService benutzerService;

    @GetMapping("/")
    public String startseite() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        Benutzer benutzer = getAktuellenBenutzer(session);
        if (benutzer == null) {
            return "redirect:/auth/anmelden";
        }

        List<Kleidungsstueck> kleidungsstuecke = kleidungsstueckService.holeAlleKleidungsstueckeNachBenutzer(benutzer);
        List<Kleidungsstueck> niedrigerLagerbestand = kleidungsstueckService.holeArtikelMitNiedrigemLagerbestand(benutzer);
        Integer gesamtLagerbestand = kleidungsstueckService.berechneGesamtLagerbestand(benutzer);
        Long ausverkaufteArtikel = kleidungsstueckService.zaehleAusverkaufteArtikel(benutzer);

        model.addAttribute("kleidungsstuecke", kleidungsstuecke);
        model.addAttribute("niedrigerLagerbestand", niedrigerLagerbestand);
        model.addAttribute("gesamtLagerbestand", gesamtLagerbestand);
        model.addAttribute("ausverkaufteArtikel", ausverkaufteArtikel);
        model.addAttribute("benutzer", benutzer);

        return "dashboard";
    }

    @GetMapping("/kleidungsstueck/neu")
    public String neuesKleidungsstueckFormular(Model model, HttpSession session) {
        if (getAktuellenBenutzer(session) == null) {
            return "redirect:/auth/anmelden";
        }

        model.addAttribute("kleidungsstueck", new Kleidungsstueck());
        model.addAttribute("groessen", Kleidungsstueck.Groesse.values());
        model.addAttribute("kategorien", Kleidungsstueck.Kategorie.values());
        return "kleidungsstueck/formular";
    }

    @PostMapping("/kleidungsstueck/speichern")
    public String speichereKleidungsstueck(@Valid @ModelAttribute Kleidungsstueck kleidungsstueck,
                                           BindingResult bindingResult,
                                           HttpSession session,
                                           RedirectAttributes umleitungsAttribute) {

        Benutzer benutzer = getAktuellenBenutzer(session);
        if (benutzer == null) {
            return "redirect:/auth/anmelden";
        }

        if (bindingResult.hasErrors()) {
            return "kleidungsstueck/formular";
        }

        kleidungsstueck.setBenutzer(benutzer);
        try {
            kleidungsstueckService.speichereKleidungsstueck(kleidungsstueck);
            umleitungsAttribute.addFlashAttribute("erfolg", "Kleidungsstück erfolgreich gespeichert!");
        } catch (RuntimeException e) {
            umleitungsAttribute.addFlashAttribute("fehler", e.getMessage());
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/kleidungsstueck/bearbeiten/{id}")
    public String bearbeiteKleidungsstueckFormular(@PathVariable Long id, Model model, HttpSession session) {
        Benutzer benutzer = getAktuellenBenutzer(session);
        if (benutzer == null) {
            return "redirect:/auth/anmelden";
        }

        Optional<Kleidungsstueck> optionalKleidungsstueck = kleidungsstueckService.findeNachId(id);
        if (optionalKleidungsstueck.isPresent()) {
            Kleidungsstueck kleidungsstueck = optionalKleidungsstueck.get();
            if (kleidungsstueck.getBenutzer().getId().equals(benutzer.getId())) {
                model.addAttribute("kleidungsstueck", kleidungsstueck);
                model.addAttribute("groessen", Kleidungsstueck.Groesse.values());
                model.addAttribute("kategorien", Kleidungsstueck.Kategorie.values());
                return "kleidungsstueck/formular";
            }
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/kleidungsstueck/loeschen/{id}")
    public String loescheKleidungsstueck(@PathVariable Long id,
                                         HttpSession session,
                                         RedirectAttributes umleitungsAttribute) {

        Benutzer benutzer = getAktuellenBenutzer(session);
        if (benutzer == null) {
            return "redirect:/auth/anmelden";
        }

        Optional<Kleidungsstueck> optionalKleidungsstueck = kleidungsstueckService.findeNachId(id);
        if (optionalKleidungsstueck.isPresent()) {
            Kleidungsstueck kleidungsstueck = optionalKleidungsstueck.get();
            if (kleidungsstueck.getBenutzer().getId().equals(benutzer.getId())) {
                kleidungsstueckService.loescheKleidungsstueck(id);
                umleitungsAttribute.addFlashAttribute("erfolg", "Kleidungsstück erfolgreich gelöscht!");
            }
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/kleidungsstueck/suche")
    public String sucheKleidungsstueck(@RequestParam(required = false) String suche,
                                       @RequestParam(required = false) String groesse,
                                       @RequestParam(required = false) String kategorie,
                                       Model model, HttpSession session) {

        Benutzer benutzer = getAktuellenBenutzer(session);
        if (benutzer == null) {
            return "redirect:/auth/anmelden";
        }

        List<Kleidungsstueck> kleidungsstuecke;

        if (suche != null && !suche.trim().isEmpty()) {
            kleidungsstuecke = kleidungsstueckService.sucheKleidungsstuecke(benutzer, suche);
        } else if (groesse != null && !groesse.isEmpty()) {
            kleidungsstuecke = kleidungsstueckService.filterNachGroesse(benutzer, Kleidungsstueck.Groesse.valueOf(groesse));
        } else if (kategorie != null && !kategorie.isEmpty()) {
            kleidungsstuecke = kleidungsstueckService.filterNachKategorie(benutzer, Kleidungsstueck.Kategorie.valueOf(kategorie));
        } else {
            kleidungsstuecke = kleidungsstueckService.holeAlleKleidungsstueckeNachBenutzer(benutzer);
        }

        model.addAttribute("kleidungsstuecke", kleidungsstuecke);
        model.addAttribute("suchbegriff", suche);
        model.addAttribute("ausgewaehlteGroesse", groesse);
        model.addAttribute("ausgewaehlteKategorie", kategorie);
        model.addAttribute("groessen", Kleidungsstueck.Groesse.values());
        model.addAttribute("kategorien", Kleidungsstueck.Kategorie.values());
        model.addAttribute("benutzer", benutzer);

        return "kleidungsstueck/suche";
    }

    private Benutzer getAktuellenBenutzer(HttpSession session) {
        Long benutzerId = (Long) session.getAttribute("benutzerId");
        if (benutzerId != null) {
            return benutzerService.findeNachId(benutzerId).orElse(null);
        }
        return null;
    }
}