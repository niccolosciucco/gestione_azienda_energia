package bw_team7.gestione_azienda_energia.clienti.repositories;

import bw_team7.gestione_azienda_energia.clienti.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
}
