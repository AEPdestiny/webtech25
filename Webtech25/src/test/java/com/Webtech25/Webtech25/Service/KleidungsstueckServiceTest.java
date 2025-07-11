package com.Webtech25.Webtech25.Service;

import com.Webtech25.Webtech25.Entity.Benutzer;
import com.Webtech25.Webtech25.Entity.Kleidungsstueck;
import com.Webtech25.Webtech25.Repository.KleidungsstueckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static com.Webtech25.Webtech25.Entity.Kleidungsstueck.Groesse.L;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KleidungsstueckServiceTest {

    private KleidungsstueckRepository kleidungsstueckRepository;
    private KleidungsstueckService service;
    private Benutzer testBenutzer;

    @BeforeEach
    void setUp() {
        kleidungsstueckRepository = Mockito.mock(KleidungsstueckRepository.class);
        service = new KleidungsstueckService();
        service.kleidungsstueckRepository = kleidungsstueckRepository; // falls nicht über Konstruktor
        testBenutzer = new Benutzer();
        testBenutzer.setId(1L);
    }

    @Test
    void testSpeichereKleidungsstueck() {
        Kleidungsstueck k = new Kleidungsstueck("T-Shirt", L, 10, testBenutzer);
        when(kleidungsstueckRepository.save(k)).thenReturn(k);

        Kleidungsstueck result = service.speichereKleidungsstueck(k);

        assertNotNull(result);
        assertEquals("T-Shirt", result.getBezeichnung());
        verify(kleidungsstueckRepository, times(1)).save(k);
    }

    @Test
    void testFindeNachId() {
        Kleidungsstueck k = new Kleidungsstueck("Hose", L, 5, testBenutzer);
        when(kleidungsstueckRepository.findById(1L)).thenReturn(Optional.of(k));

        Optional<Kleidungsstueck> result = service.findeNachId(1L);

        assertTrue(result.isPresent());
        assertEquals("Hose", result.get().getBezeichnung());
    }

    @Test
    void testLoescheKleidungsstueck() {
        doNothing().when(kleidungsstueckRepository).deleteById(1L);
        service.loescheKleidungsstueck(1L);
        verify(kleidungsstueckRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFilterNachGroesse() {
        Kleidungsstueck k = new Kleidungsstueck("Jacke", L, 2, testBenutzer);
        when(kleidungsstueckRepository.findByBenutzerAndGroesse(testBenutzer, L))
                .thenReturn(List.of(k));

        List<Kleidungsstueck> result = service.filterNachGroesse(testBenutzer, L);

        assertEquals(1, result.size());
        assertEquals("Jacke", result.get(0).getBezeichnung());
    }
}
