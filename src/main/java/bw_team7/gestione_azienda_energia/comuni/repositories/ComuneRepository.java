package bw_team7.gestione_azienda_energia.comuni.repositories;

import bw_team7.gestione_azienda_energia.comuni.entities.Comune;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ComuneRepository extends JpaRepository<Comune, UUID> {
}
