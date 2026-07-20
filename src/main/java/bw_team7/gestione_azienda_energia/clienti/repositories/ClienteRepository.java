package bw_team7.gestione_azienda_energia.clienti.repositories;

import bw_team7.gestione_azienda_energia.clienti.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
}
