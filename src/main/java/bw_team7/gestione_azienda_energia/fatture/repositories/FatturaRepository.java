package bw_team7.gestione_azienda_energia.fatture.repositories;

import bw_team7.gestione_azienda_energia.fatture.entities.Fattura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FatturaRepository extends JpaRepository<Fattura, UUID>, JpaSpecificationExecutor<Fattura> {
    boolean existsByNumeroAndClienteId(Integer numero, UUID clienteId);
}
