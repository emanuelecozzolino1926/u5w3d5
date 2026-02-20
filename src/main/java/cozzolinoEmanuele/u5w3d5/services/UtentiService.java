package cozzolinoEmanuele.u5w3d5.services;

import cozzolinoEmanuele.u5w3d5.entities.Utente;
import cozzolinoEmanuele.u5w3d5.exceptions.BadRequestException;
import cozzolinoEmanuele.u5w3d5.exceptions.NotFoundException;
import cozzolinoEmanuele.u5w3d5.payloads.NuovoUtenteDTO;
import cozzolinoEmanuele.u5w3d5.repositories.UtentiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UtentiService {

    private final UtentiRepository utentiRepository;
    private final PasswordEncoder bcrypt;

    @Autowired
    public UtentiService(UtentiRepository utentiRepository, PasswordEncoder bcrypt) {
        this.utentiRepository = utentiRepository;
        this.bcrypt = bcrypt;
    }

    public Utente save(NuovoUtenteDTO payload) {
        this.utentiRepository.findByEmail(payload.email()).ifPresent(u -> {
            throw new BadRequestException("L'email " + payload.email() + " è già in uso!");
        });

        Utente nuovoUtente = new Utente(payload.nome(), payload.cognome(), payload.email(), bcrypt.encode(payload.password()));

        if (payload.ruolo() != null) nuovoUtente.setRuolo(payload.ruolo());

        Utente savedUtente = this.utentiRepository.save(nuovoUtente);

        log.info("L'utente con id " + savedUtente.getId() + " è stato salvato correttamente!");

        return savedUtente;
    }

    public Utente findById(UUID utenteId) {
        return this.utentiRepository.findById(utenteId)
                .orElseThrow(() -> new NotFoundException(utenteId));
    }

    public Utente findByEmail(String email) {
        return this.utentiRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("L'utente con email " + email + " non è stato trovato!"));
    }
}
