package cozzolinoEmanuele.u5w3d5.services;

import cozzolinoEmanuele.u5w3d5.entities.Evento;
import cozzolinoEmanuele.u5w3d5.entities.Utente;
import cozzolinoEmanuele.u5w3d5.exceptions.BadRequestException;
import cozzolinoEmanuele.u5w3d5.exceptions.NotFoundException;
import cozzolinoEmanuele.u5w3d5.exceptions.UnauthorizedException;
import cozzolinoEmanuele.u5w3d5.payloads.NuovoEventoDTO;
import cozzolinoEmanuele.u5w3d5.repositories.EventiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class EventiService {

    private final EventiRepository eventiRepository;

    @Autowired
    public EventiService(EventiRepository eventiRepository) {
        this.eventiRepository = eventiRepository;
    }

    public Page<Evento> findAll(int page, int size, String orderBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.eventiRepository.findAll(pageable);
    }

    public Evento findById(UUID eventoId) {
        return this.eventiRepository.findById(eventoId)
                .orElseThrow(() -> new NotFoundException(eventoId));
    }

    public Evento save(NuovoEventoDTO payload, Utente organizzatore) {
        Evento nuovoEvento = new Evento(payload.titolo(), payload.descrizione(), payload.data(),
                payload.luogo(), payload.postiTotali(), organizzatore);
        Evento savedEvento = this.eventiRepository.save(nuovoEvento);

        log.info("L'evento con id " + savedEvento.getId() + " è stato salvato correttamente!");

        return savedEvento;
    }

    public Evento findByIdAndUpdate(UUID eventoId, NuovoEventoDTO payload, Utente currentUser) {
        Evento found = this.findById(eventoId);

        if (!found.getOrganizzatore().getId().equals(currentUser.getId()))
            throw new UnauthorizedException("Non puoi modificare un evento che non hai creato!");

        int postiPrenotati = found.getPostiTotali() - found.getPostiDisponibili();
        if (payload.postiTotali() < postiPrenotati)
            throw new BadRequestException("Non puoi ridurre i posti totali al di sotto delle prenotazioni già effettuate!");

        found.setTitolo(payload.titolo());
        found.setDescrizione(payload.descrizione());
        found.setData(payload.data());
        found.setLuogo(payload.luogo());
        found.setPostiDisponibili(payload.postiTotali() - postiPrenotati);
        found.setPostiTotali(payload.postiTotali());

        return this.eventiRepository.save(found);
    }

    public void findByIdAndDelete(UUID eventoId, Utente currentUser) {
        Evento found = this.findById(eventoId);

        if (!found.getOrganizzatore().getId().equals(currentUser.getId()))
            throw new UnauthorizedException("Non puoi eliminare un evento che non hai creato!");

        this.eventiRepository.delete(found);
    }

    public void decrementaPostiDisponibili(Evento evento) {
        if (evento.getPostiDisponibili() <= 0)
            throw new BadRequestException("Non ci sono più posti disponibili per questo evento!");
        evento.setPostiDisponibili(evento.getPostiDisponibili() - 1);
        this.eventiRepository.save(evento);
    }

    public void incrementaPostiDisponibili(Evento evento) {
        evento.setPostiDisponibili(evento.getPostiDisponibili() + 1);
        this.eventiRepository.save(evento);
    }
}
