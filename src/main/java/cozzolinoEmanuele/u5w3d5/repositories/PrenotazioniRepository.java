package cozzolinoEmanuele.u5w3d5.repositories;

import cozzolinoEmanuele.u5w3d5.entities.Evento;
import cozzolinoEmanuele.u5w3d5.entities.Prenotazione;
import cozzolinoEmanuele.u5w3d5.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrenotazioniRepository extends JpaRepository<Prenotazione, UUID> {
    List<Prenotazione> findByUtente(Utente utente);

    boolean existsByUtenteAndEvento(Utente utente, Evento evento);
}
