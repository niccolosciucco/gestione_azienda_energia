package bw_team7.gestione_azienda_energia.clienti.repositories;

import bw_team7.gestione_azienda_energia.clienti.entities.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    boolean existsByPartitaIva(String partitaIva);

    Page<Cliente> findByFatturatoAnnuale(BigDecimal fatturatoAnnuale, Pageable pageable);

    Page<Cliente> findByDataInserimento(LocalDate dataInserimento, Pageable pageable);

    Page<Cliente> findByDataUltimoContatto(LocalDate dataUltimoContatto, Pageable pageable);

    Page<Cliente> findByRagioneSocialeContainingIgnoreCase(String nome, Pageable pageable);
}
