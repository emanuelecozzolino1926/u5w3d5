package cozzolinoEmanuele.u5w3d5.repositories;

import cozzolinoEmanuele.u5w3d5.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventiRepository extends JpaRepository<Evento, UUID> {
}
