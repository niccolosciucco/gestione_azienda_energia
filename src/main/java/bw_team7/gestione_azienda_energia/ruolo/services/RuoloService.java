package bw_team7.gestione_azienda_energia.ruolo.services;

import bw_team7.gestione_azienda_energia.exceptions.custom.NotFound;
import bw_team7.gestione_azienda_energia.ruolo.entities.Ruolo;
import bw_team7.gestione_azienda_energia.ruolo.repositories.RuoloRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RuoloService {

    private final RuoloRepository ruoloRepository;

    public RuoloService(RuoloRepository ruoloRepository) {
        this.ruoloRepository = ruoloRepository;
    }

    // Cerca un ruolo per nome
    public Ruolo findByNome(String nome) {
        return this.ruoloRepository.findByNome(nome)
                .orElseThrow(() -> new NotFound("Ruolo con nome '" + nome + "' non trovato!"));
    }

    // Metodo di comodo per recuperare rapidamente il ruolo di default
    public Ruolo getRuoloUser() {
        return findByNome("ROLE_USER");
    }

    // Cerca un ruolo per ID
    public Ruolo findById(UUID id) {
        return this.ruoloRepository.findById(id)
                .orElseThrow(() -> new NotFound("Ruolo con ID " + id + " non trovato!"));
    }

    // Salva un nuovo ruolo nel database
    public Ruolo save(Ruolo ruolo) {
        return this.ruoloRepository.save(ruolo);
    }

    // Recupera tutti i ruoli presenti
    public List<Ruolo> findAll() {
        return this.ruoloRepository.findAll();
    }
}
