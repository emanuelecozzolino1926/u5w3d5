package cozzolinoEmanuele.u5w3d5.controllers;

import cozzolinoEmanuele.u5w3d5.entities.Prenotazione;
import cozzolinoEmanuele.u5w3d5.entities.Utente;
import cozzolinoEmanuele.u5w3d5.services.PrenotazioniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class PrenotazioniController {

    private final PrenotazioniService prenotazioniService;

    @Autowired
    public PrenotazioniController(PrenotazioniService prenotazioniService) {
        this.prenotazioniService = prenotazioniService;
    }

    @PostMapping("/eventi/{eventoId}/prenotazioni")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('UTENTE_NORMALE')")
    public Prenotazione save(@PathVariable UUID eventoId,
                             @AuthenticationPrincipal Utente currentUser) {
        return this.prenotazioniService.save(eventoId, currentUser);
    }

    @GetMapping("/prenotazioni/me")
    public List<Prenotazione> findMyBookings(@AuthenticationPrincipal Utente currentUser) {
        return this.prenotazioniService.findByUtente(currentUser);
    }

    @DeleteMapping("/prenotazioni/{prenotazioneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('UTENTE_NORMALE')")
    public void findByIdAndDelete(@PathVariable UUID prenotazioneId,
                                  @AuthenticationPrincipal Utente currentUser) {
        this.prenotazioniService.findByIdAndDelete(prenotazioneId, currentUser);
    }
}
