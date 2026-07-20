package bw_team7.gestione_azienda_energia.province.repositories;

import bw_team7.gestione_azienda_energia.province.entities.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, UUID> {
    Optional<Provincia> findByNome(String nome);

    boolean existsByNomeOrSigla(String nome, String sigla);
}
