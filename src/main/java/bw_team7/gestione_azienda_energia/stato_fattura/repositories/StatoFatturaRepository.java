package bw_team7.gestione_azienda_energia.stato_fattura.repositories;

import bw_team7.gestione_azienda_energia.stato_fattura.entities.StatoFattura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StatoFatturaRepository extends JpaRepository<StatoFattura, UUID> {
}
