package bw_team7.gestione_azienda_energia.ruolo.repositories;

import bw_team7.gestione_azienda_energia.ruolo.entities.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RuoloRepository extends JpaRepository<Ruolo, UUID> {
    Optional<Ruolo> findByNome(String nome);

    boolean existsByNome(String nome);
}
