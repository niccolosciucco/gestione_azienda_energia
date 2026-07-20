package bw_team7.gestione_azienda_energia.utenti.repositories;

import bw_team7.gestione_azienda_energia.utenti.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, UUID> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<Utente> findByEmail(String email);

    Optional<Utente> findByUsername(String username);
}