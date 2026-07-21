package bw_team7.gestione_azienda_energia.indirizzi.repository;

import bw_team7.gestione_azienda_energia.indirizzi.entities.Indirizzo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IndirizzoRepository extends JpaRepository<Indirizzo, UUID> {
    boolean existsByViaAndCivicoAndCapAndComuneId(String via, String civico, String cap, UUID comuneId);
}
