package cozzolinoEmanuele.u5w3d5.services;

import cozzolinoEmanuele.u5w3d5.entities.Evento;
import cozzolinoEmanuele.u5w3d5.entities.Prenotazione;
import cozzolinoEmanuele.u5w3d5.entities.Utente;
import cozzolinoEmanuele.u5w3d5.exceptions.BadRequestException;
import cozzolinoEmanuele.u5w3d5.exceptions.NotFoundException;
import cozzolinoEmanuele.u5w3d5.exceptions.UnauthorizedException;
import cozzolinoEmanuele.u5w3d5.repositories.PrenotazioniRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PrenotazioniService {

    private final PrenotazioniRepository prenotazioniRepository;
    private final EventiService eventiService;

    @Autowired
    public PrenotazioniService(PrenotazioniRepository prenotazioniRepository, EventiService eventiService) {
        this.prenotazioniRepository = prenotazioniRepository;
        this.eventiService = eventiService;
    }

    public Prenotazione save(UUID eventoId, Utente currentUser) {

        Evento evento = this.eventiService.findById(eventoId);

        if (this.prenotazioniRepository.existsByUtenteAndEvento(currentUser, evento))
            throw new BadRequestException("Hai già prenotato un posto per questo evento!");

        this.eventiService.decrementaPostiDisponibili(evento);

        Prenotazione nuovaPrenotazione = new Prenotazione(currentUser, evento);
        Prenotazione savedPrenotazione = this.prenotazioniRepository.save(nuovaPrenotazione);

        log.info("La prenotazione con id " + savedPrenotazione.getId() + " è stata salvata correttamente!");

        return savedPrenotazione;
    }

    public List<Prenotazione> findByUtente(Utente currentUser) {
        return this.prenotazioniRepository.findByUtente(currentUser);
    }

    public void findByIdAndDelete(UUID prenotazioneId, Utente currentUser) {

        Prenotazione found = this.prenotazioniRepository.findById(prenotazioneId)
                .orElseThrow(() -> new NotFoundException(prenotazioneId));

        if (!found.getUtente().getId().equals(currentUser.getId()))
            throw new UnauthorizedException("Non puoi cancellare una prenotazione che non ti appartiene!");

        this.eventiService.incrementaPostiDisponibili(found.getEvento());
        this.prenotazioniRepository.delete(found);
    }
}
